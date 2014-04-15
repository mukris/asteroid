package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.control.ControlInterface;
import hu.bme.mit.asteroid.control.MiscControlInterface;
import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.player.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Egy játékmenetet reprezentáló osztály. A játékmenet egy játék elindításától
 * kezdve GameOver-ig, vagy valamilyen hibáig tart, bele tartozik a pályák
 * közötti átmenet is.
 */
public abstract class GameSession implements ControlInterface.Callback {

	public enum State {
		LEVEL_STARTING, LEVEL_COMPLETE, GAME_OVER, RUNNING, PAUSED, STOPPED, ERROR
	}

	protected GameState mGameState;
	protected GameRunner mGameRunner;
	protected Player mPlayer1;
	protected State mState;
	protected State mOldState;
	protected int mLevelID;

	public GameSession(Player player, int levelID) {
		mPlayer1 = player;
		mLevelID = levelID;
		mState = State.LEVEL_STARTING;
	}
	
	public void setMiscControlInterface(MiscControlInterface miscControlInterface) {
		miscControlInterface.setCallback(this);
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

	@Override
	public void control(ControlEvent event) {
		switch (event.getType()) {
		case INVERT_PAUSE:
			if (mState != State.PAUSED) {
				mOldState = mState;
				pause();
			} else {
				start();
				mState = mOldState;
			}
			break;

		default:
			break;
		}
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

	/**
	 * A játék fizikáját és grafikáját külön szálon ütemező osztály
	 */
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
							calculatePhysics(timeDelta, currentTime);
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

		protected void calculatePhysics(long timeDelta, long currentTime) {
			calculateAsteroidPhysics(timeDelta, currentTime);
			calculateSpaceShipPhysics(mGameState.getSpaceShip1(), timeDelta, currentTime);
			
			if (mGameState.isMultiplayer()) {
				calculateSpaceShipPhysics(mGameState.getSpaceShip2(), timeDelta, currentTime);
			}
			calculateWeaponPhysics(timeDelta, currentTime);
			// TODO mozgás, ütközésvizsgálat
		}

		protected void calculateSpaceShipPhysics(SpaceShip spaceShip, long timeDelta, long currentTime) {
			// FIXME: Bűvészkedés az alábbi (és hasonló) függvényekkel:
			// spaceShip.getAcceleration();
			// spaceShip.getSpeed();
			// spaceShip.getPosition();
			// spaceShip.setPosition(position);
			// az új pozíció meghatározása a paraméterül kapott
			// időkülönbség és a pillanatnyi sebesség, gyorsulás alapján
		}

		protected void calculateAsteroidPhysics(long timeDelta, long currentTime) {
			ArrayList<Asteroid> asteroids = mGameState.getAsteroids();

			for (Asteroid asteroid : asteroids) {
				// TODO: lásd fentebb...
				// asteroid.getPosition();
				// asteroid.getSpeed();
				// asteroid.setPosition(position);
			}
			// TODO
		}

		protected void calculateWeaponPhysics(long timeDelta, long currentTime) {
			// TODO
		}

		protected void updateGUI() {
			GameManager.getInstance().updateGameField(mGameState);
		}
	}
}
