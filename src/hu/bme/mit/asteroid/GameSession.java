package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.control.ControlInterface;
import hu.bme.mit.asteroid.control.MiscControlInterface;
import hu.bme.mit.asteroid.exceptions.GameOverException;
import hu.bme.mit.asteroid.exceptions.LevelFinishedException;
import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.Vector2D;
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

	/**
	 * A játékmenet lehetséges állapotait reprezentáló enum
	 */
	public enum State {
		LEVEL_STARTING, LEVEL_COMPLETE, GAME_OVER, RUNNING, PAUSED, STOPPED, ERROR
	}

	protected GameState mGameState;
	protected GameRunner mGameRunner;
	protected Player mPlayer1;
	protected State mState;
	protected State mOldState;
	protected int mLevelID;

	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	public GameSession(Player player, int levelID) {
		mPlayer1 = player;
		mLevelID = levelID;
		mState = State.LEVEL_STARTING;
	}

	/**
	 * A kiegészítő vezérlőparancsokra való feliratkozás (pl: pause mód)
	 * 
	 * @param miscControlInterface
	 */
	public void setMiscControlInterface(MiscControlInterface miscControlInterface) {
		miscControlInterface.setCallback(this);
	}

	/**
	 * A {@link GameRunner} indítása vagy újraindítása
	 */
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

	/**
	 * A {@link GameRunner} pillanatnyi megállítása. Újraindítani a
	 * {@link #start()} ismételt meghívásával lehet.
	 */
	public synchronized void pause() {
		if (mGameRunner != null) {
			mGameRunner.setRunning(false);
		}
		mState = State.PAUSED;
	}

	/**
	 * A {@link GameRunner} leállítása és felszámolása
	 */
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

	/**
	 * A {@link GameState} frissítése az új adatok alapján
	 * 
	 * @param newGameState
	 */
	public void updateGameState(GameState newGameState) {
		synchronized (mGameState) {
			mGameState.update(newGameState);
		}
	}

	/**
	 * A {@link GameRunner} indítása
	 */
	protected void startGameRunner() {
		if (mGameRunner == null || !mGameRunner.isAlive()) {
			mGameRunner = newGameRunner();
			mGameRunner.start();
		} else {
			logger.severe("startGameRunner hibás állapot...(fixme)");
		}
	}

	/**
	 * Új {@link GameRunner} példányosítása. A leszármazott osztályok
	 * felüldefiniálhatják, hogy speciális GameRunner leszármazottakat hozzanak
	 * létre.
	 * 
	 * @return Egy új GameRunner példány
	 */
	protected GameRunner newGameRunner() {
		return new GameRunner();
	}

	/**
	 * A játék fizikáját és grafikáját külön szálon ütemező osztály
	 */
	protected class GameRunner extends Thread {

		private AtomicBoolean mRunning = new AtomicBoolean(true);
		private long mLastTime;

		@Override
		public void run() {
			long currentTime, timeDelta;
			mLastTime = System.currentTimeMillis();
			while (true) {
				try {
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
				} catch (InterruptedException e) {
					return;
				} catch (LevelFinishedException e) {
					// TODO mit csinálunk ha vége a pályának
					logger.info("Level finished");
				} catch (GameOverException e) {
					// TODO mit csinálunk GameOvernél..
					logger.info("GameOver");
					return;
				}
			}
		}

		public void setRunning(boolean running) {
			mRunning.set(running);
		}

		/**
		 * A legutolsó futás idejének kézi beállítása. Abban az esetben fontos,
		 * amikor a játékot megállította a játékos, hiszen ha ezt az értéket nem
		 * állítjuk át, egy pillanat alatt lezajlik minden olyan mozgás, ami a
		 * szünet ideje alatt történt volna.
		 * 
		 * @param time
		 *            A beállítandó idő (valószínűleg az aktuális rendszeridő)
		 */
		public void setLastTime(long time) {
			mLastTime = time;
		}

		/**
		 * A játék fizikai számításait kezelő függvény
		 * 
		 * @param timeDelta
		 *            A függvény utolsó futtatása óta eltelt idő
		 *            ezredmásodpercben
		 * @param currentTime
		 *            Az aktuális rendszeridő ezredmásodpercben
		 * @throws LevelFinishedException
		 *             Abban az esetben, ha az adott pálya végére értünk,
		 *             elfogytak az aszteroidák
		 * @throws GameOverException
		 *             Abban az esetben, ha az űrhajó megsemmisült
		 */
		protected void calculatePhysics(long timeDelta, long currentTime) throws LevelFinishedException,
				GameOverException {
			calculateAsteroidPhysics(timeDelta, currentTime);
			calculateSpaceShipPhysics(mGameState.getSpaceShip1(), timeDelta, currentTime);

			if (mGameState.isMultiplayer()) {
				calculateSpaceShipPhysics(mGameState.getSpaceShip2(), timeDelta, currentTime);
			}
			calculateWeaponPhysics(timeDelta, currentTime);
			// TODO mozgás, ütközésvizsgálat
		}

		/**
		 * Az űrhajó modelljének fizikai számításait végző függvény
		 * 
		 * @param spaceShip
		 *            Az űrhajó
		 * @param timeDelta
		 *            A függvény utolsó futtatása óta eltelt idő
		 *            ezredmásodpercben
		 * @param currentTime
		 *            Az aktuális rendszeridő ezredmásodpercben
		 * @throws GameOverException
		 *             Abban az esetben, ha az űrhajó megsemmisült
		 */
		protected void calculateSpaceShipPhysics(SpaceShip spaceShip, long timeDelta, long currentTime)
				throws GameOverException {
			// FIXME: Bűvészkedés az alábbi (és hasonló) függvényekkel:
			// spaceShip.getAcceleration();
			// spaceShip.getSpeed();
			// spaceShip.getPosition();
			// spaceShip.setPosition(position);
			// az új pozíció meghatározása a paraméterül kapott
			// időkülönbség és a pillanatnyi sebesség, gyorsulás alapján
			// valami ilyesmi...
			spaceShip.setPosition(spaceShip.getPosition().add(new Vector2D(0.1f, 0)));
		}

		/**
		 * Az aszteroidák fizikai számításait végző függvény
		 * 
		 * @param timeDelta
		 *            A függvény utolsó futtatása óta eltelt idő
		 *            ezredmásodpercben
		 * @param currentTime
		 *            Az aktuális rendszeridő ezredmásodpercben
		 * @throws LevelFinishedException
		 *             Abban az esetben, ha az adott pálya végére értünk,
		 *             elfogytak az aszteroidák
		 */
		protected void calculateAsteroidPhysics(long timeDelta, long currentTime) throws LevelFinishedException {
			ArrayList<Asteroid> asteroids = mGameState.getAsteroids();

			if (asteroids.isEmpty()) {
				// TODO: kommentet kiszedni, ha már vannak aszteroidák a
				// GameState-ben
				// throw new LevelFinishedException();
			}

			for (Asteroid asteroid : asteroids) {
				// TODO: lásd fentebb...
				// asteroid.getPosition();
				// asteroid.getSpeed();
				// asteroid.setPosition(position);
			}
			// TODO
		}

		/**
		 * A fegyverek fizikai számításait végző függvény
		 * 
		 * @param timeDelta
		 *            A függvény utolsó futtatása óta eltelt idő
		 *            ezredmásodpercben
		 * @param currentTime
		 *            Az aktuális rendszeridő ezredmásodpercben
		 */
		protected void calculateWeaponPhysics(long timeDelta, long currentTime) {
			// TODO
		}

		/**
		 * A grafikus felület frissítése
		 */
		protected void updateGUI() {
			GameManager.getInstance().updateGameField(mGameState);
		}
	}
}
