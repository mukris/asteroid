package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.exceptions.LevelNotUnlockedException;
import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.Asteroid.Type;
import hu.bme.mit.asteroid.model.SimpleWeapon;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.Vector2D;
import hu.bme.mit.asteroid.player.Player;

import java.util.ArrayList;

/**
 * A {@link GameFactory} osztály felelős egy játékmező összeállításáért
 */
public class GameFactory {
	/**
	 * A pályák általános leírását tartalmazó tömb
	 */
	private static ArrayList<LevelDescriptor> sLevels = null;
	private static int mGameFieldWidth = 0;
	private static int mGameFieldHeight = 0;

	/**
	 * Inicializálás
	 */
	static {
		sLevels = new ArrayList<>();
		sLevels.add(new LevelDescriptor(0, 0, 3, true)); // első pálya feloldva
		sLevels.add(new LevelDescriptor(0, 1, 4));
		sLevels.add(new LevelDescriptor(0, 3, 2));
		sLevels.add(new LevelDescriptor(0, 6, 0));
		sLevels.add(new LevelDescriptor(1, 4, 0));
		sLevels.add(new LevelDescriptor(2, 4, 0));
		sLevels.add(new LevelDescriptor(3, 4, 0));
		sLevels.add(new LevelDescriptor(4, 0, 0));
		sLevels.add(new LevelDescriptor(3, 0, 6));
		sLevels.add(new LevelDescriptor(4, 4, 4));

		updateLevelUnlockStatus();
	}

	/**
	 * Visszaadja, hogy hány pálya van a játékban
	 * 
	 * @return A pályák száma
	 */
	public static int getLevelCount() {
		return sLevels.size();
	}

	/**
	 * Új játéktér létrehozása a megadott szinten egyjátékos módban
	 * 
	 * @param levelID
	 *            A létrehozni kívánt pálya szintje
	 * @param player
	 *            A játékost reprezentáló objektum
	 * @return Az újonnan létrehozott játéktér
	 * @throws LevelNotExistsException
	 *             Akkor dobja, ha a levelID érvénytelen
	 * @throws LevelNotUnlockedException
	 *             Akkor dobja, ha az adott pálya még nincs feloldva
	 */
	public static GameState createSingleplayerGame(int levelID, Player player) throws LevelNotExistsException,
			LevelNotUnlockedException {
		if (levelID < 0 || levelID > sLevels.size() - 1) {
			throw new LevelNotExistsException();
		}
		updateLevelUnlockStatus();
		if (!sLevels.get(levelID).isUnlocked()) {
			throw new LevelNotUnlockedException();
		}

		updateGameFieldDimensions();

		SpaceShip spaceShip = new SpaceShip(new Vector2D(mGameFieldWidth / 2, mGameFieldHeight / 2), Math.PI / 2,
				new SimpleWeapon());
		spaceShip.setUnvulnerableFor(SpaceShip.DEFAULT_UNVULNERABILITY_TIME);
		player.setSpaceShip(spaceShip);
		GameState gameState = new GameState(player, null);
		generateAsteroidPositions(gameState, sLevels.get(levelID));

		return gameState;
	}

	/**
	 * Új játéktér létrehozása a megadott szinten kétjátékos módban
	 * 
	 * @param levelID
	 *            A létrehozni kívánt pálya szintje
	 * @param player1
	 *            Az egyik játékost reprezentáló objektum
	 * @param player2
	 *            A másik játékost reprezentáló objektum
	 * @return Az újonnan létrehozott játéktér
	 * @throws LevelNotExistsException
	 *             Akkor dobja, ha a levelID érvénytelen
	 */
	public static GameState createMultiplayerGame(int levelID, Player player1, Player player2)
			throws LevelNotExistsException {
		if (levelID < 0 || levelID > sLevels.size() - 1) {
			throw new LevelNotExistsException();
		}

		updateGameFieldDimensions();

		SpaceShip spaceShip1 = new SpaceShip(new Vector2D(mGameFieldWidth / 3, mGameFieldHeight / 2), Math.PI / 2,
				new SimpleWeapon());
		spaceShip1.setUnvulnerableFor(SpaceShip.DEFAULT_UNVULNERABILITY_TIME);
		player1.setSpaceShip(spaceShip1);

		SpaceShip spaceShip2 = new SpaceShip(new Vector2D(mGameFieldWidth - mGameFieldWidth / 3, mGameFieldHeight / 2),
				Math.PI / 2, new SimpleWeapon());
		spaceShip2.setUnvulnerableFor(SpaceShip.DEFAULT_UNVULNERABILITY_TIME);
		player2.setSpaceShip(spaceShip2);
		GameState gameState = new GameState(player1, player2);
		generateAsteroidPositions(gameState, sLevels.get(levelID));

		return gameState;
	}

	/**
	 * A pálya leírásában szereplő paramétereknek megfelelő számú aszteroida
	 * létrehozása, kezdeti paramétereinek (véletlenszerű) beállítása
	 * 
	 * @param gameState
	 *            A játékteret leíró objektum
	 * @param levelDescriptor
	 *            A pályát leíró objektum
	 */
	private static void generateAsteroidPositions(GameState gameState, LevelDescriptor levelDescriptor) {
		ArrayList<Asteroid> asteroids = gameState.getAsteroids();
		synchronized (asteroids) {
			for (int i = 0; i < levelDescriptor.getNumAsteroidLarge(); i++) {
				asteroids.add(createNewRandomAsteroid(Type.LARGE));
			}
			for (int i = 0; i < levelDescriptor.getNumAsteroidMedium(); i++) {
				asteroids.add(createNewRandomAsteroid(Type.MEDIUM));
			}
			for (int i = 0; i < levelDescriptor.getNumAsteroidSmall(); i++) {
				asteroids.add(createNewRandomAsteroid(Type.SMALL));
			}
		}
	}

	/**
	 * Új {@link Asteroid} generálása véletlenszerű pozícióval és sebességgel
	 * 
	 * @param type
	 *            Az aszteroida típusa
	 * @return Új véletlenszerűen elhelyezett aszteroida
	 */
	private static Asteroid createNewRandomAsteroid(Asteroid.Type type) {
		float minSpeed, maxSpeed;

		switch (type) {
		case LARGE:
			maxSpeed = Asteroid.ASTEROID_SPEED_LARGE_MAX;
			minSpeed = Asteroid.ASTEROID_SPEED_LARGE_MIN;
			break;
		case MEDIUM:
			maxSpeed = Asteroid.ASTEROID_SPEED_MEDIUM_MAX;
			minSpeed = Asteroid.ASTEROID_SPEED_MEDIUM_MIN;
			break;
		case SMALL:
			maxSpeed = Asteroid.ASTEROID_SPEED_SMALL_MAX;
			minSpeed = Asteroid.ASTEROID_SPEED_SMALL_MIN;
			break;

		default:
			throw new RuntimeException();
		}

		return new Asteroid(type, Vector2D.generateRandomPosition(mGameFieldWidth, mGameFieldHeight,
				Asteroid.ASTEROID_MIN_DISTANCE), Vector2D.generateRandom(minSpeed, maxSpeed));
	}

	/**
	 * Frissíti a játéktér méreteit
	 */
	private static void updateGameFieldDimensions() {
		mGameFieldWidth = GameManager.getInstance().getGameFieldWidth();
		mGameFieldHeight = GameManager.getInstance().getGameFieldHeight();
	}

	/**
	 * A pályák feloldottsági állapotának frissítése
	 */
	private static void updateLevelUnlockStatus() {
		for (int i = 0; i < sLevels.size(); i++) {
			int highestUnlockedLevel = Storage.getInstance().getHighestUnlockedLevel();
			sLevels.get(i).setUnlocked(i <= highestUnlockedLevel);
		}
	}
}
