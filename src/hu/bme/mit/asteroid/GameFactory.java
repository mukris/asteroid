package hu.bme.mit.asteroid;

import java.util.ArrayList;

public class GameFactory {
	
	private static GameFactory sInstance = null;
	
	private static ArrayList<LevelDescriptor> sLevels = null;
	
	private GameFactory()
	{
		sLevels = new ArrayList<>();
		sLevels.add(new LevelDescriptor(0, 5, 0, true)); // first level is always unlocked...
		sLevels.add(new LevelDescriptor(1, 2, 0));
		// TODO define levels, load unlocked level info from file
	}
	
	public static GameFactory getInstance() {
		if(sInstance == null) {
			sInstance = new GameFactory();
		}
		return sInstance;
	}

	public GameState createSingleplayerGame(int levelID, Player player) {
		// TODO check levelID, unlock status
		GameState gameState = generateAsteroidPositions(sLevels.get(levelID), false);
		// TODO initialize Player, SpaceShip...
		return gameState;
	}
	
	public GameState createLocalMultiplayerGame(int levelID, Player player1, Player player2) {
		// TODO check levelID
		GameState gameState = generateAsteroidPositions(sLevels.get(levelID), true);
		// TODO initialize Players, SpaceShips...
		return gameState;
	}
	
	private GameState generateAsteroidPositions(LevelDescriptor levelDescriptor, boolean isMultiplayer) {
		GameState gameState = new GameState();
		// TODO randomize Asteroids' position, speed..
		return gameState;
	}
}
