package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.Weapon;
import hu.bme.mit.asteroid.player.Player;

import java.util.ArrayList;

/**
 * A játék midenkori aktuális állapotát tároló osztály
 */
public class GameState {

	private Player.State mPlayer1State;
	private SpaceShip mSpaceShip1;

	private Player.State mPlayer2State = null;
	private SpaceShip mSpaceShip2 = null;

	private ArrayList<Asteroid> mAsteroids;
	private ArrayList<Weapon> mWeapons;

	public GameState(Player player1, Player player2) {
		mPlayer1State = player1.getState();
		mSpaceShip1 = player1.getSpaceShip();

		if (player2 != null) {
			mPlayer2State = player2.getState();
			mSpaceShip2 = player2.getSpaceShip();
		}
		
		mAsteroids = new ArrayList<>();
		mWeapons = new ArrayList<>();
	}
	
	public boolean isMultiplayer() {
		return (mPlayer2State != null);
	}
}
