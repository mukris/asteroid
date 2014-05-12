package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.exceptions.GameOverException;
import hu.bme.mit.asteroid.exceptions.LevelFinishedException;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.network.NetworkClient;
import hu.bme.mit.asteroid.network.NetworkListener;
import hu.bme.mit.asteroid.network.NetworkServer;
import hu.bme.mit.asteroid.player.Player;

/**
 * A kétjátékos játék menetét irányító osztály
 */
public class MultiplayerGameSession extends GameSession {

	/**
	 * A kétjátékos játéktípusok
	 */
	public enum Type {
		LOCAL, NETWORK_SERVER, NETWORK_CLIENT
	}

	private Type mType;
	private Player mPlayer2;
	private NetworkListener mNetworkListener = null;
	private NetworkServer mNetworkServer = null;
	private NetworkClient mNetworkClient = null;

	public MultiplayerGameSession(Type type, Player player1, Player player2, int levelID)
			throws LevelNotExistsException {
		super(player1, levelID);
		mType = type;
		mPlayer2 = player2;
		mGameState = GameFactory.createMultiplayerGame(levelID, player1, player2);

		GameManager gameManager = GameManager.getInstance();
		if (mType == Type.NETWORK_SERVER) {
			mNetworkServer = gameManager.getNetworkServer();
		} else if (mType == Type.NETWORK_CLIENT) {
			mNetworkClient = gameManager.getNetworkClient();
		}
	}

	/**
	 * A {@link MultiplayerGameSession} egyetlen {@link NetworkListener}
	 * páldányának elkérése
	 * 
	 * @return A NetworkListener példány
	 */
	public NetworkListener getNetworkListener() {
		if (mNetworkListener == null) {
			mNetworkListener = new NetworkListener() {

				@Override
				public void onDisconnect() {
					GameManager gameManager = GameManager.getInstance();
					gameManager.unregisterClientListener(MultiplayerGameSession.this);
					gameManager.unregisterServerListener(MultiplayerGameSession.this);
					
					if (getState() != GameSession.State.GAME_OVER) {
						stop();
						gameManager.onNetworkError();
						setState(State.ERROR);
					}
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
			return new MultiplayerGameRunner();
		}
	}

	/**
	 * A játék fizikáját és grafikáját két játékos esetén külön szálon ütemező
	 * osztály
	 */
	private class MultiplayerGameRunner extends GameRunner {
		@Override
		protected void calculatePhysics(long timeDelta, long currentTime) throws LevelFinishedException,
				GameOverException {
			calculateAsteroidPhysics(timeDelta, currentTime);
			calculateSpaceShipPhysics(mGameState.getSpaceShip1(), timeDelta, currentTime);
			calculateSpaceShipPhysics(mGameState.getSpaceShip2(), timeDelta, currentTime);
			calculateWeaponPhysics(mGameState.getSpaceShip1(), timeDelta, currentTime);
			calculateWeaponPhysics(mGameState.getSpaceShip2(), timeDelta, currentTime);
		}

		@Override
		protected void checkCollisions() throws GameOverException {
			checkWeapon2AsteroidCollision(mGameState.getSpaceShip1(), mGameState.getPlayer1State());
			checkWeapon2AsteroidCollision(mGameState.getSpaceShip2(), mGameState.getPlayer2State());
			checkSpaceship2AsteroidCollisions(mGameState.getSpaceShip1(), mGameState.getPlayer1State());
			checkSpaceship2AsteroidCollisions(mGameState.getSpaceShip2(), mGameState.getPlayer2State());
			checkSpaceship2PowerupCollision(mGameState.getSpaceShip1(), mGameState.getPlayer1State());
			checkSpaceship2PowerupCollision(mGameState.getSpaceShip2(), mGameState.getPlayer2State());
		}

		@Override
		protected void loadNextLevel() throws GameOverException {
			try {
				mLevelID++;
				GameState newGameState = GameFactory.createMultiplayerGame(mLevelID, mPlayer1, mPlayer2);
				mGameState.update(newGameState);
			} catch (LevelNotExistsException e) {
				throw new GameOverException();
			}
		}
	}

	/**
	 * A szerver oldalon a játékot futtató szál. Az állást elküldjük a
	 * kliensnek.
	 */
	private class ServerGameRunner extends MultiplayerGameRunner {
		@Override
		protected void calculatePhysics(long timeDelta, long currentTime) throws LevelFinishedException,
				GameOverException {
			super.calculatePhysics(timeDelta, currentTime);
			mNetworkServer.sendGameState(mGameState);
		}

		@Override
		protected void onGameOver() {
			super.onGameOver();
			mNetworkServer.sendGameState(mGameState);
			mNetworkServer.disconnect();
		}
	}

	/**
	 * A kliens oldalon a játékot futtató szál. Itt nem számolunk fizikát.
	 */
	private class ClientGameRunner extends MultiplayerGameRunner {
		@Override
		public void run() {
			while (true) {
				try {
					setState(mGameState.getGameSessionState());
					if (mGameState.getGameSessionState() == GameSession.State.GAME_OVER) {
						return;
					}

					sleep(50);
				} catch (InterruptedException e) {
					return;
				}
			}
		}

		@Override
		protected void onGameOver() {
			super.onGameOver();
			mNetworkClient.disconnect();
		}
	}
}
