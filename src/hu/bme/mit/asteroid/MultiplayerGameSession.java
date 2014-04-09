package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.network.NetworkListener;
import hu.bme.mit.asteroid.player.Player;

public class MultiplayerGameSession extends GameSession {

	public enum Type {
		LOCAL, NETWORK_SERVER, NETWORK_CLIENT
	}

	private Type mType;
	private Player mPlayer2;
	private NetworkListener mNetworkListener = null;

	public MultiplayerGameSession(Type type, Player player1, Player player2, int levelID)
			throws LevelNotExistsException {
		super(player1, levelID);
		mPlayer2 = player2;
		mGameState = GameFactory.getInstance().createMultiplayerGame(levelID, player1, player2);
	}

	public NetworkListener getNetworkListener() {
		if (mNetworkListener == null) {
			mNetworkListener = new NetworkListener() {

				@Override
				public void onDisconnect() {
					// TODO
					GameManager.getInstance().unregisterClientListener(MultiplayerGameSession.this);
					GameManager.getInstance().unregisterServerListener(MultiplayerGameSession.this);
					mState = State.ERROR;
				}

				@Override
				public void onReceive(ControlEvent event) {
					// TODO Auto-generated method stub
					super.onReceive(event);
				}

				@Override
				public void onReceive(GameState gameState) {
					updateGameState(gameState);
				}
			};
		}
		return mNetworkListener;
	}

	@Override
	protected GameRunner newGameRunner() {
		switch (mType) {
		case NETWORK_CLIENT:
			return new ClientGameRunner();

		case LOCAL:
		case NETWORK_SERVER:
		default:
			return super.newGameRunner();
		}
	}

	/**
	 * A kliens oldalon a játékot futtató szál. Itt nem számolunk fizikát.
	 */
	private class ClientGameRunner extends GameRunner {
		@Override
		protected void calculatePhysics(long timeDelta, long currentTime) {
			return;
		}
	}

}
