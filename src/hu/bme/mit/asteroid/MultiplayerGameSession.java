package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.exceptions.GameOverException;
import hu.bme.mit.asteroid.exceptions.LevelFinishedException;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.network.NetworkListener;
import hu.bme.mit.asteroid.network.NetworkServer;
import hu.bme.mit.asteroid.player.Player;

public class MultiplayerGameSession extends GameSession {

	public enum Type {
		LOCAL, NETWORK_SERVER, NETWORK_CLIENT
	}

	private Type mType;
	private Player mPlayer2;
	private NetworkListener mNetworkListener = null;
	private NetworkServer mNetworkServer = null;

	public MultiplayerGameSession(Type type, Player player1, Player player2, int levelID)
			throws LevelNotExistsException {
		super(player1, levelID);
		mType = type;
		mPlayer2 = player2;
		mGameState = GameFactory.createMultiplayerGame(levelID, player1, player2);

		if (mType == Type.NETWORK_SERVER) {
			mNetworkServer = GameManager.getInstance().getNetworkServer();
		}
	}

	public NetworkListener getNetworkListener() {
		if (mNetworkListener == null) {
			mNetworkListener = new NetworkListener() {

				@Override
				public void onDisconnect() {
					// TODO
					stop();
					GameManager gameManager = GameManager.getInstance();
					gameManager.unregisterClientListener(MultiplayerGameSession.this);
					gameManager.unregisterServerListener(MultiplayerGameSession.this);
					gameManager.onNetworkError();
					mState = State.ERROR;
				}

				@Override
				public void onReceive(GameState gameState) {
					updateGameState(gameState);
					GameManager.getInstance().updateGameField(mGameState);
				}
			};
		}
		return mNetworkListener;
	}

	@Override
	public void control(ControlEvent event) {
		switch (event.getType()) {
		case INVERT_PAUSE:
			switch (mType) {
			case NETWORK_CLIENT:
			case NETWORK_SERVER:
				return;

			default:
				super.control(event);
				break;
			}
			break;

		default:
			super.control(event);
			break;
		}
	}

	@Override
	protected GameRunner newGameRunner() {
		switch (mType) {
		case NETWORK_CLIENT:
			return new ClientGameRunner();

		case NETWORK_SERVER:
			return new ServerGameRunner();

		case LOCAL:
		default:
			return super.newGameRunner();
		}
	}

	/**
	 * A szerver oldalon a játékot futtató szál. Az állást elküldjük a
	 * kliensnek.
	 */
	private class ServerGameRunner extends GameRunner {
		@Override
		protected void calculatePhysics(long timeDelta, long currentTime) throws LevelFinishedException,
				GameOverException {
			super.calculatePhysics(timeDelta, currentTime);
			mNetworkServer.sendGameState(mGameState);
		}
	}

	/**
	 * A kliens oldalon a játékot futtató szál. Itt nem számolunk fizikát.
	 */
	private class ClientGameRunner extends GameRunner {
		@Override
		public void run() {
			return;
		}
	}
}
