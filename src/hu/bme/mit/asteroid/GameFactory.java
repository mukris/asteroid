package hu.bme.mit.asteroid;

import java.util.ArrayList;

/**
 * A {@link GameFactory} osztály felelős egy játékmező összeállításáért
 */
public class GameFactory {

	/**
	 * Singleton példány
	 */
	private static GameFactory sInstance = null;

	/**
	 * A pályák általános leírását tartalmazó tömb
	 */
	private static ArrayList<LevelDescriptor> sLevels = null;

	/**
	 * Privát konstruktor (mivel Singleton osztály)
	 */
	private GameFactory() {
		sLevels = new ArrayList<>();
		sLevels.add(new LevelDescriptor(0, 5, 0, true)); // első pálya feloldva
		sLevels.add(new LevelDescriptor(1, 2, 0));
		// TODO define levels, load unlocked level info from file
	}

	/**
	 * A {@link GameFactory} osztály egyetlen (Singleton) példányának lekérése
	 * 
	 * @return Az egyetlen GameFactory példány
	 */
	public static GameFactory getInstance() {
		if (sInstance == null) {
			sInstance = new GameFactory();
		}
		return sInstance;
	}

	/**
	 * Új játéktér létrehozása a megadott szinten egyjátékos módban
	 * 
	 * @param levelID
	 *            A létrehozni kívánt pálya szintje
	 * @param player
	 *            A játékost reprezentáló objektum
	 * @return Az újonnan létrehozott játéktér
	 */
	public GameState createSingleplayerGame(int levelID, Player player) {
		// TODO check levelID, unlock status
		// TODO initialize Player, SpaceShip...
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
	 */
	public GameState createMultiplayerGame(int levelID, Player player1, Player player2) {
		// TODO check levelID
		// TODO initialize Players, SpaceShips...
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
	private void generateAsteroidPositions(GameState gameState, LevelDescriptor levelDescriptor) {
		boolean isMultiplayer = gameState.isMultiplayer();
		// TODO create Asteroids
		// TODO randomize Asteroids' position, speed..
	}
}
