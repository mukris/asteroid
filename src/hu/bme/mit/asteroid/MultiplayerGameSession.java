package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.network.NetworkListener;
import hu.bme.mit.asteroid.player.Player;

public class MultiplayerGameSession extends GameSession {

	private Player mPlayer2;
	private NetworkListener mNetworkListener = null;

	public MultiplayerGameSession(Player player1, Player player2, int levelID) throws LevelNotExistsException {
		super(player1, levelID);
		mPlayer2 = player2;
		mGameState = GameFactory.getInstance().createMultiplayerGame(levelID, player1, player2);
	}

	public NetworkListener getNetworkListener() {
		if (mNetworkListener == null) {
			mNetworkListener = new NetworkListener() {
				
				@Override
				public void onDisconnect() {
					//TODO
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
		return new MultiplayerGameRunner();
	}

	private class MultiplayerGameRunner extends GameRunner {
		@Override
		protected void calculateSpaceShipPhysics(long timeDelta) {
			super.calculateSpaceShipPhysics(timeDelta);
			
			//TODO: a második űrhajóra is számoljunk...
		}
	}

}
