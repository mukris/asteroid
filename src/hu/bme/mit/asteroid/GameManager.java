package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.MultiplayerGameSession.Type;
import hu.bme.mit.asteroid.control.ArrowControlInterface;
import hu.bme.mit.asteroid.control.MiscControlInterface;
import hu.bme.mit.asteroid.control.WADControlInterface;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.exceptions.LevelNotUnlockedException;
import hu.bme.mit.asteroid.gui.GameField;
import hu.bme.mit.asteroid.network.NetworkClient;
import hu.bme.mit.asteroid.network.NetworkDiscover;
import hu.bme.mit.asteroid.network.NetworkServer;
import hu.bme.mit.asteroid.player.DummyPlayer;
import hu.bme.mit.asteroid.player.LocalPlayer;
import hu.bme.mit.asteroid.player.NetworkLocalPlayer;
import hu.bme.mit.asteroid.player.NetworkRemotePlayer;
import hu.bme.mit.asteroid.player.Player;

/**
 * A játékmenetet irányító osztály
 */
public class GameManager {
	private GameField mGameField;
	private GameSession mGameSession;
	private NetworkServer mNetworkServer = null;
	private NetworkClient mNetworkClient = null;
	private NetworkDiscover mNetworkDiscover = null;

	private static GameManager sInstance = null;

	public GameManager(GameField gameField) {
		if (sInstance != null) {
			throw new RuntimeException(
					"GameManager has already been initialized. Use the getInstance() method to get the existing instance");
		}
		mGameField = gameField;
		sInstance = this;
	}

	public static GameManager getInstance() {
		if (sInstance == null) {
			throw new RuntimeException("GameManager must be initialized first!");
		}
		return sInstance;
	}

	public NetworkServer getNetworkServer() {
		if (mNetworkServer == null) {
			mNetworkServer = new NetworkServer();
		}
		return mNetworkServer;
	}

	public NetworkClient getNetworkClient() {
		if (mNetworkClient == null) {
			mNetworkClient = new NetworkClient();
		}
		return mNetworkClient;
	}

	public NetworkDiscover getNetworkDiscover() {
		if (mNetworkDiscover == null) {
			mNetworkDiscover = new NetworkDiscover();
		}
		return mNetworkDiscover;
	}

	public void startSinglePlayerGame(int levelID) throws LevelNotExistsException, LevelNotUnlockedException {
		ArrowControlInterface controlInterface = new ArrowControlInterface();
		MiscControlInterface miscControlInterface = new MiscControlInterface();

		mGameField.addKeyListener(controlInterface);
		mGameField.addKeyListener(miscControlInterface);

		Player player = new LocalPlayer(controlInterface);

		mGameSession = new SingleplayerGameSession(player, levelID);
		mGameSession.setMiscControlInterface(miscControlInterface);
		mGameSession.start();
	}

	public void startLocalMultiplayerGame() throws LevelNotExistsException {
		ArrowControlInterface arrowControlInterface = new ArrowControlInterface();
		WADControlInterface wadControlInterface = new WADControlInterface();
		MiscControlInterface miscControlInterface = new MiscControlInterface();

		mGameField.addKeyListener(arrowControlInterface);
		mGameField.addKeyListener(wadControlInterface);
		mGameField.addKeyListener(miscControlInterface);

		Player player1 = new LocalPlayer(arrowControlInterface);
		Player player2 = new LocalPlayer(wadControlInterface);

		mGameSession = new MultiplayerGameSession(Type.LOCAL, player1, player2, 0);
		mGameSession.setMiscControlInterface(miscControlInterface);
		mGameSession.start();
	}

	public void startNetworkServerMultiplayerGame() throws LevelNotExistsException {
		ArrowControlInterface arrowControlInterface = new ArrowControlInterface();
		MiscControlInterface miscControlInterface = new MiscControlInterface();

		mGameField.addKeyListener(arrowControlInterface);
		mGameField.addKeyListener(miscControlInterface);

		LocalPlayer player1 = new LocalPlayer(arrowControlInterface);
		NetworkRemotePlayer player2 = new NetworkRemotePlayer();

		mNetworkServer.addReceiveListener(player2);

		MultiplayerGameSession gameSession = new MultiplayerGameSession(Type.NETWORK_SERVER, player1, player2, 0);
		registerAsServerListener(gameSession);
		mGameSession = gameSession;
		mGameSession.setMiscControlInterface(miscControlInterface);
		mGameSession.start();
	}

	public void startNetworkClientMultiplayerGame() throws LevelNotExistsException {
		ArrowControlInterface arrowControlInterface = new ArrowControlInterface();
		MiscControlInterface miscControlInterface = new MiscControlInterface();

		mGameField.addKeyListener(arrowControlInterface);
		mGameField.addKeyListener(miscControlInterface);

		NetworkLocalPlayer player1 = new NetworkLocalPlayer(arrowControlInterface, mNetworkClient);
		Player player2 = new DummyPlayer();

		MultiplayerGameSession gameSession = new MultiplayerGameSession(Type.NETWORK_CLIENT, player1, player2, 0);
		registerAsClientListener(gameSession);
		mGameSession = gameSession;
		mGameSession.setMiscControlInterface(miscControlInterface);
		mGameSession.start();
	}

	public void registerAsServerListener(MultiplayerGameSession gameSession) {
		mNetworkServer.addConnectionListener(gameSession.getNetworkListener());
		mNetworkServer.addReceiveListener(gameSession.getNetworkListener().asControlEventReceiver());
	}

	public void registerAsClientListener(MultiplayerGameSession gameSession) {
		mNetworkClient.addConnectionListener(gameSession.getNetworkListener());
		mNetworkClient.addReceiveListener(gameSession.getNetworkListener().asGameStateReceiver());
	}

	public void unregisterServerListener(MultiplayerGameSession gameSession) {
		mNetworkServer.removeConnectionListener(gameSession.getNetworkListener());
		mNetworkServer.removeReceiveListener(gameSession.getNetworkListener().asControlEventReceiver());
	}

	public void unregisterClientListener(MultiplayerGameSession gameSession) {
		mNetworkClient.removeConnectionListener(gameSession.getNetworkListener());
		mNetworkClient.removeReceiveListener(gameSession.getNetworkListener().asGameStateReceiver());
	}

	public void updateGameField(GameState gameState) {
		mGameField.update(gameState);
	}

	public void onNetworkError() {
		mGameField.onNetworkError();
	}
}
