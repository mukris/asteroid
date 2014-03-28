package hu.bme.mit.asteroid;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import hu.bme.mit.asteroid.player.Player;

/**
 * Egy játékmenetet reprezentáló osztály. A játékmenet egy játék elindításától
 * kezdve GameOver-ig, vagy valamilyen hibáig tart, bele tartozik a pályák
 * közötti átmenet is.
 */
public abstract class GameSession {

	public enum State {
		LEVEL_STARTING, LEVEL_COMPLETE, GAME_OVER, RUNNING, PAUSED, STOPPED, ERROR
	}

	protected GameState mGameState;
	protected GameRunner mGameRunner;
	protected Player mPlayer1;
	protected State mState;
	protected int mLevelID;

	public GameSession(Player player, int levelID) {
		mPlayer1 = player;
		mLevelID = levelID;
		mState = State.LEVEL_STARTING;
	}

	public synchronized void start() {
		if (mState == State.PAUSED && mGameRunner != null) {
			synchronized (mGameRunner) {
				mGameRunner.setLastTime(System.currentTimeMillis());
				mGameRunner.setRunning(true);
				mGameRunner.notify();
			}
		} else {
			startGameRunner();
		}

		mState = State.RUNNING;
	}

	public synchronized void pause() {
		if (mGameRunner != null) {
			mGameRunner.setRunning(false);
		}
		mState = State.PAUSED;
	}

	public synchronized void stop() {
		if (mGameRunner != null) {
			synchronized (mGameRunner) {
				mGameRunner.setRunning(false);
				mGameRunner.interrupt();
				try {
					mGameRunner.join();
					mGameRunner = null;
				} catch (InterruptedException e) {
				}
			}
		}
		mState = State.STOPPED;
	}

	public void updateGameState(GameState newGameState) {
		synchronized (mGameState) {
			mGameState.update(newGameState);
		}
	}

	protected void startGameRunner() {
		if (mGameRunner == null || !mGameRunner.isAlive()) {
			mGameRunner = newGameRunner();
			mGameRunner.start();
		} else {
			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.severe("startGameRunner hibás állapot...(fixme)");
		}
	}

	protected GameRunner newGameRunner() {
		return new GameRunner();
	}

	protected class GameRunner extends Thread {

		private AtomicBoolean mRunning;
		private long mLastTime;

		public GameRunner() {
			mRunning.set(true);
		}

		@Override
		public void run() {
			long currentTime, timeDelta;
			mLastTime = System.currentTimeMillis();
			try {
				while (true) {
					if (!mRunning.get()) {
						synchronized (this) {
							wait();
						}
					} else {
						synchronized (mGameState) {
							currentTime = System.currentTimeMillis();
							timeDelta = currentTime - mLastTime;
							if (timeDelta < 10) {
								sleep(10); // ne járassuk 100%-on a procit...
								continue;
							}
							calculatePhysics(timeDelta);
							updateGUI();
							setLastTime(currentTime);
						}
					}
				}
			} catch (InterruptedException e) {
				return;
			}
		}

		public void setRunning(boolean running) {
			mRunning.set(running);
		}

		public void setLastTime(long time) {
			mLastTime = time;
		}

		protected void calculatePhysics(long timeDelta) {
			calculateAsteroidPhysics();
			calculateSpaceShipPhysics();
			calculateWeaponPhysics();
			// TODO mozgás, ütközésvizsgálat
		}

		protected void calculateSpaceShipPhysics() {
			// TODO
		}

		protected void calculateAsteroidPhysics() {
			// TODO
		}

		protected void calculateWeaponPhysics() {
			// TODO
		}

		protected void updateGUI() {
			GameManager.getInstance().updateGameField(mGameState);
		}
	}
}
