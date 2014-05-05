package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.control.ControlInterface;
import hu.bme.mit.asteroid.control.MiscControlInterface;
import hu.bme.mit.asteroid.exceptions.GameOverException;
import hu.bme.mit.asteroid.exceptions.LevelFinishedException;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.exceptions.LevelNotUnlockedException;
import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.Asteroid.Type;
import hu.bme.mit.asteroid.model.Powerup;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.Vector2D;
import hu.bme.mit.asteroid.model.Weapon;
import hu.bme.mit.asteroid.player.Player;

import java.util.ArrayList;
import java.util.List;
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

	/**
	 * A {@link GameSession} állapotváltozásait jelző interfész
	 */
	public interface GameSessionListener {
		/**
		 * Akkor hívódik, ha megváltozott a {@link GameSession} {@link State}-je
		 * 
		 * @param state
		 *            Az új állapot
		 */
		public void onStateChange(State state);
	}

	protected List<GameSessionListener> mListeners;
	protected GameState mGameState;
	protected GameRunner mGameRunner;
	protected Player mPlayer1;
	protected State mState;
	protected State mOldState;
	protected int mLevelID;

	protected int mWidth;
	protected int mHeight;

	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	public GameSession(Player player, int levelID) {
		mPlayer1 = player;
		mLevelID = levelID;
		mState = State.LEVEL_STARTING;
		mListeners = new ArrayList<>();
	}

	/**
	 * A kiegészítő vezérlőparancsokra való feliratkozás (pl: pause mód)
	 * 
	 * @param miscControlInterface
	 */
	public void setMiscControlInterface(MiscControlInterface miscControlInterface) {
		miscControlInterface.setCallback(this);
	}

	public void addGameSessionListener(GameSessionListener listener) {
		if (listener != null) {
			synchronized (mListeners) {
				mListeners.add(listener);
			}
		}
	}

	public void removeGameSessionListener(GameSessionListener listener) {
		synchronized (mListeners) {
			mListeners.remove(listener);
		}
	}

	public State getState() {
		synchronized (mState) {
			return mState;
		}
	}

	public void setState(State state) {
		synchronized (mState) {
			mState = state;
			synchronized (mListeners) {
				for (GameSessionListener listener : mListeners) {
					listener.onStateChange(mState);
				}
			}
		}
	}

	/**
	 * A játékmező méretének beállítása a fizikai motor számításaihoz
	 * 
	 * @param width
	 *            A játékmező szélessége
	 * @param height
	 *            A játékmező magassága
	 */
	public void setDimensions(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	/**
	 * A {@link GameRunner} indítása vagy újraindítása
	 */
	public synchronized void start() {
		if (getState() == State.PAUSED && mGameRunner != null) {
			synchronized (mGameRunner) {
				mGameRunner.setLastTime(System.currentTimeMillis());
				mGameRunner.setRunning(true);
				mGameRunner.notify();
			}
		} else {
			startGameRunner();
		}

		setState(State.RUNNING);
	}

	/**
	 * A {@link GameRunner} pillanatnyi megállítása. Újraindítani a
	 * {@link #start()} ismételt meghívásával lehet.
	 */
	public synchronized void pause() {
		if (mGameRunner != null) {
			mGameRunner.setRunning(false);
		}
		setState(State.PAUSED);
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
		setState(State.STOPPED);
	}

	@Override
	public void control(ControlEvent event) {
		switch (event.getType()) {
		case INVERT_PAUSE:
			if (getState() != State.PAUSED) {
				mOldState = getState();
				pause();
			} else {
				start();
				setState(mOldState);
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
						if (GameSession.this.getState() == GameSession.State.LEVEL_COMPLETE) {
							loadNextLevel();
							setState(GameSession.State.LEVEL_STARTING);
						}
						synchronized (mGameState) {
							currentTime = System.currentTimeMillis();
							timeDelta = currentTime - mLastTime;
							if (timeDelta < 10) {
								sleep(10); // ne járassuk 100%-on a procit...
								continue;
							}
							calculatePhysics(timeDelta, currentTime);
							checkCollisions();
							updateGUI();
							setLastTime(currentTime);
						}
					}
				} catch (InterruptedException e) {
					return;
				} catch (LevelFinishedException e) {
					logger.info("Level finished");
					setState(GameSession.State.LEVEL_COMPLETE);
				} catch (GameOverException e) {
					logger.info("GameOver");
					setState(GameSession.State.GAME_OVER);
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
		 * Új pálya betöltése
		 * 
		 * @throws GameOverException
		 *             Ha nincs több pálya, a játék véget ér
		 */
		protected void loadNextLevel() throws GameOverException {
			mLevelID++;
			Storage.getInstance().setLevelUnlocked(mLevelID);

			try {
				GameState newGameState = GameFactory.createSingleplayerGame(mLevelID, mPlayer1);
				mGameState.update(newGameState);
			} catch (LevelNotExistsException e) {
				throw new GameOverException();
			} catch (LevelNotUnlockedException e) {
				e.printStackTrace();
				throw new GameOverException();
			}
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

			calculateWeaponPhysics(mGameState.getSpaceShip1(), timeDelta, currentTime);

			if (mGameState.isMultiplayer()) {
				calculateWeaponPhysics(mGameState.getSpaceShip2(), timeDelta, currentTime);
			}
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
		 */
		protected void calculateSpaceShipPhysics(SpaceShip spaceShip, long timeDelta, long currentTime) {
			spaceShip.getPosition().add(spaceShip.getSpeed().clone().multiply(timeDelta / 1000f))
					.inRange(mWidth, mHeight);

			if (spaceShip.isAccelerating()) {
				Vector2D acceleration = spaceShip.getAcceleration();
				if (acceleration.getLength() < 12f) {
					acceleration.setLength(acceleration.getLength() + timeDelta);
				}

				spaceShip.getSpeed().add(spaceShip.getAcceleration().clone().multiply(timeDelta / 100f)).limit(600f);
			}

			if (spaceShip.isTurningLeft()) {
				spaceShip.setDirection((spaceShip.getDirection() + timeDelta / 1000f * Math.PI * 2));
			} else if (spaceShip.isTurningRight()) {
				spaceShip.setDirection((spaceShip.getDirection() - timeDelta / 1000f * Math.PI * 2));
			}

			spaceShip.setUnvulnerableFor(spaceShip.getUnvulnerableFor() - timeDelta);
			spaceShip.handleFiring(timeDelta);
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
			synchronized (asteroids) {
				if (asteroids.isEmpty()) {
					throw new LevelFinishedException();
				}

				for (Asteroid asteroid : asteroids) {
					asteroid.getPosition().add(asteroid.getSpeed().clone().multiply(timeDelta / 1000f))
							.inRange(mWidth, mHeight);
				}
			}
		}

		/**
		 * A fegyverek fizikai számításait végző függvény
		 * 
		 * @param spaceShip
		 *            Az űrhajó
		 * @param timeDelta
		 *            A függvény utolsó futtatása óta eltelt idő
		 *            ezredmásodpercben
		 * @param currentTime
		 *            Az aktuális rendszeridő ezredmásodpercben
		 */
		protected void calculateWeaponPhysics(SpaceShip spaceShip, long timeDelta, long currentTime) {
			List<Weapon> weapons = spaceShip.getWeapons();
			synchronized (weapons) {
				List<Weapon> deadWeapons = new ArrayList<>();
				for (Weapon weapon : weapons) {
					weapon.decreaseTimeUntilDeath(timeDelta);
					if (weapon.isAlive()) {
						Vector2D displacement = weapon.getSpeed().clone().multiply(timeDelta / 100f);
						weapon.getPosition().add(displacement).inRange(mWidth, mHeight);
					} else {
						deadWeapons.add(weapon);
					}
				}
				// A lejárt szavatosságú fegyverek kiszedése a tömbből
				for (Weapon deadWeapon : deadWeapons) {
					weapons.remove(deadWeapon);
				}
			}
		}

		/**
		 * Ütközések vizsgálata
		 * 
		 * @throws GameOverException
		 *             Ha az űrhajó aszteroidával ütközött, és a {@link Player}
		 *             -nek nincs már több élete, a játék véget ér.
		 */
		protected void checkCollisions() throws GameOverException {
			checkWeapon2AsteroidCollision(mGameState.getSpaceShip1());
			if (mGameState.isMultiplayer()) {
				checkWeapon2AsteroidCollision(mGameState.getSpaceShip2());
			}

			checkSpaceship2AsteroidCollisions(mGameState.getSpaceShip1(), mGameState.getPlayer1State());
			if (mGameState.isMultiplayer()) {
				checkSpaceship2AsteroidCollisions(mGameState.getSpaceShip2(), mGameState.getPlayer2State());
			}

			checkSpaceship2PowerupCollision(mGameState.getSpaceShip1(), mGameState.getPlayer1State());
			if (mGameState.isMultiplayer()) {
				checkSpaceship2PowerupCollision(mGameState.getSpaceShip2(), mGameState.getPlayer2State());
			}
		}

		/**
		 * {@link SpaceShip} és {@link Asteroid}ák ütközésének ellenőrzése
		 * 
		 * @param spaceShip
		 *            A vizsgált űrhajó
		 * @throws GameOverException
		 *             Ha az űrhajó aszteroidával ütközött, és a {@link Player}
		 *             -nek nincs már több élete, a játék véget ér.
		 */
		protected void checkSpaceship2AsteroidCollisions(SpaceShip spaceShip,
				Player.State state) throws GameOverException {
			ArrayList<Asteroid> asteroids = mGameState.getAsteroids();
			ArrayList<Asteroid> toRemove = new ArrayList<>();
			ArrayList<Asteroid> toAdd = new ArrayList<>();
			synchronized (asteroids) {
				for (Asteroid asteroid : asteroids) {
					if (asteroid.checkCollision(spaceShip)
							&& spaceShip.isVulnerable()) {
						synchronized (state) {
							int lives = state.getLives();
							if (--lives == 0) {
								throw new GameOverException();
							}
							state.setLives(lives);
						}
						spaceShip
								.setUnvulnerableFor(SpaceShip.DEFAULT_UNVULNERABILITY_TIME);
						switch (asteroid.getType()) {
						case LARGE:
							toAdd.add(new Asteroid(Type.MEDIUM, asteroid
									.getPosition(), Vector2D.generateRandom(
									Asteroid.ASTEROID_SPEED_MEDIUM_MIN,
									Asteroid.ASTEROID_SPEED_MEDIUM_MAX)));

							toAdd.add(new Asteroid(Type.MEDIUM, asteroid
									.getPosition(), Vector2D.generateRandom(
									Asteroid.ASTEROID_SPEED_MEDIUM_MIN,
									Asteroid.ASTEROID_SPEED_MEDIUM_MAX)));

							toRemove.add(asteroid);
							break;

						case MEDIUM:
							toAdd.add(new Asteroid(Type.SMALL, asteroid
									.getPosition(), Vector2D.generateRandom(
									Asteroid.ASTEROID_SPEED_SMALL_MIN,
									Asteroid.ASTEROID_SPEED_SMALL_MAX)));
							toAdd.add(new Asteroid(Type.SMALL, asteroid
									.getPosition(), Vector2D.generateRandom(
									Asteroid.ASTEROID_SPEED_SMALL_MIN,
									Asteroid.ASTEROID_SPEED_SMALL_MAX)));
							toRemove.add(asteroid);
							break;

						case SMALL:
							asteroids.remove(asteroid);
							break;
						}
					}
				}
			}
			return;
		}

		/**
		 * {@link SpaceShip} és {@link Powerup}ok ütközésének ellenőrzése
		 * 
		 * @param spaceShip
		 *            A vizsgált űrhajó
		 * @param state
		 *            A megfelelő játékos {@link Player.State}-je
		 */
		protected void checkSpaceship2PowerupCollision(SpaceShip spaceShip, Player.State state) {
			ArrayList<Powerup> powerups = mGameState.getPowerups();
			synchronized (powerups) {
				for (Powerup powerup : powerups) {
					if (powerup.checkCollision(spaceShip)) {
						switch (powerup.getType()) {
						case PLUS_POINTS_SMALL:
							state.addPoints(10);
							break;
						case PLUS_POINTS_LARGE:
							state.addPoints(50);
							break;
						case UNVULNERABILITY:
							spaceShip.setUnvulnerableFor(SpaceShip.DEFAULT_UNVULNERABILITY_TIME);
							break;
						case PLUS_LIFE:
							state.setLives(state.getLives() + 1);
							break;
						}
						powerups.remove(powerup);
						return;
					}
				}
			}
		}

		/**
		 * A {@link SpaceShip} minden {@link Weapon}-jének és {@link Asteroid}ák
		 * ütközésének ellenőrzése
		 * 
		 * @param spaceShip
		 *            Az űrhajó, aminek a lövedékeit vizsgáljuk
		 */
		protected void checkWeapon2AsteroidCollision(SpaceShip spaceShip) {
			List<Weapon> weapons = spaceShip.getWeapons();
			List<Powerup> powerups = mGameState.getPowerups();
			ArrayList<Asteroid> asteroids = mGameState.getAsteroids();
			ArrayList<Asteroid> toRemove = new ArrayList<>();
			ArrayList<Asteroid> toAdd = new ArrayList<>();
			synchronized (weapons) {
				for (Weapon weapon : weapons) {
					synchronized (asteroids) {
						for (Asteroid asteroid : asteroids) {
							if (asteroid.checkCollision(weapon) && weapon.isAlive()) {
								// HACK: ha a lövedék ütközik, azonnal
								// semmisüljön meg -> hátralévő idejét
								// csökkentjük
								weapon.decreaseTimeUntilDeath(Weapon.LIFE_SPAN_MILLIS);

								if (asteroid.getHitsLeft() > 1) {
									asteroid.setHitsLeft(asteroid.getHitsLeft() - 1);
								} else {
									switch (asteroid.getType()) {
									case LARGE:
										toAdd.add(new Asteroid(Type.MEDIUM, asteroid.getPosition(), Vector2D
												.generateRandom(Asteroid.ASTEROID_SPEED_MEDIUM_MIN,
														Asteroid.ASTEROID_SPEED_MEDIUM_MAX)));

										toAdd.add(new Asteroid(Type.MEDIUM, asteroid.getPosition(), Vector2D
												.generateRandom(Asteroid.ASTEROID_SPEED_MEDIUM_MIN,
														Asteroid.ASTEROID_SPEED_MEDIUM_MAX)));
										

										toRemove.add(asteroid);
										break;

									case MEDIUM:
										toAdd.add(new Asteroid(Type.SMALL, asteroid.getPosition(), Vector2D
												.generateRandom(Asteroid.ASTEROID_SPEED_SMALL_MIN,
														Asteroid.ASTEROID_SPEED_SMALL_MAX)));
										toAdd.add(new Asteroid(Type.SMALL, asteroid.getPosition(), Vector2D
												.generateRandom(Asteroid.ASTEROID_SPEED_SMALL_MIN,
														Asteroid.ASTEROID_SPEED_SMALL_MAX)));
										toRemove.add(asteroid);
										break;

									case SMALL:
										toRemove.add(asteroid);
										break;
									}
									
									Powerup possiblePowerup = Powerup.tryLuck(asteroid.getPosition().clone());
									if(possiblePowerup != null){
										synchronized (powerups) {
											powerups.add(possiblePowerup);
										}
									}
								}
							}
						}
					}
				}
			}
			synchronized (asteroids) {
				for (Asteroid asteroid : toRemove) {
					asteroids.remove(asteroid);
				}
				for (Asteroid asteroid : toAdd) {
					asteroids.add(asteroid);
				}
			}
		}

		/**
		 * A grafikus felület frissítése
		 */
		protected void updateGUI() {
			GameManager.getInstance().updateGameField(mGameState);
		}
	}
}
