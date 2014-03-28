package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.exceptions.LevelNotUnlockedException;
import hu.bme.mit.asteroid.player.Player;

public class SingleplayerGameSession extends GameSession {

	public SingleplayerGameSession(Player player, int levelID) throws LevelNotExistsException,
			LevelNotUnlockedException {
		super(player, levelID);
		mGameState = GameFactory.getInstance().createSingleplayerGame(levelID, player);
	}

}
