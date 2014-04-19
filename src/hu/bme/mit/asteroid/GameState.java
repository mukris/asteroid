package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.Powerup;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.Weapon;
import hu.bme.mit.asteroid.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A játék midenkori aktuális állapotát tároló osztály
 */
public class GameState implements Serializable {
	private static final long serialVersionUID = -6065362868807499848L;
	
	private Player.State mPlayer1State;
	private SpaceShip mSpaceShip1;

	private Player.State mPlayer2State = null;
	private SpaceShip mSpaceShip2 = null;

	private ArrayList<Asteroid> mAsteroids;
	private ArrayList<Weapon> mWeapons;
	private ArrayList<Powerup> mPowerups;

	public GameState(Player player1, Player player2) {
		mPlayer1State = player1.getState();
		mSpaceShip1 = player1.getSpaceShip();

		if (player2 != null) {
			mPlayer2State = player2.getState();
			mSpaceShip2 = player2.getSpaceShip();
		}

		mAsteroids = new ArrayList<>();
		mWeapons = new ArrayList<>();
		mPowerups = new ArrayList<>();
	}

	/**
	 * Frissíti az aktuális állapotot új adatokkal
	 * 
	 * @param newGameState
	 */
	public void update(GameState newGameState) {
		synchronized (mPlayer1State) {
			mPlayer1State = newGameState.mPlayer1State;
		}
		synchronized (mSpaceShip1) {
			mSpaceShip1 = newGameState.mSpaceShip1;
		}
		if (mPlayer2State != null) {
			synchronized (mPlayer2State) {
				mPlayer2State = newGameState.mPlayer2State;
			}
		}
		if (mSpaceShip2 != null) {
			synchronized (mSpaceShip2) {
				mSpaceShip2 = newGameState.mSpaceShip2;
			}
		}
		synchronized (mAsteroids) {
			mAsteroids = newGameState.mAsteroids;
		}
		synchronized (mWeapons) {
			mWeapons = newGameState.mWeapons;
		}
		synchronized (mPowerups) {
			mPowerups = newGameState.mPowerups;
		}
	}

	public boolean isMultiplayer() {
		return (mPlayer2State != null);
	}

	public Player.State getPlayer1State() {
		synchronized (mPlayer1State) {
			return mPlayer1State;
		}
	}

	public Player.State getPlayer2State() {
		if (mPlayer2State == null) {
			return null;
		}
		synchronized (mPlayer2State) {
			return mPlayer2State;
		}
	}

	public SpaceShip getSpaceShip1() {
		synchronized (mSpaceShip1) {
			return mSpaceShip1;
		}
	}

	public SpaceShip getSpaceShip2() {
		if (mSpaceShip2 == null) {
			return null;
		}
		synchronized (mSpaceShip2) {
			return mSpaceShip2;
		}
	}

	public ArrayList<Asteroid> getAsteroids() {
		synchronized (mAsteroids) {
			return mAsteroids;
		}
	}

	public ArrayList<Weapon> getWeapons() {
		synchronized (mWeapons) {
			return mWeapons;
		}
	}

	public ArrayList<Powerup> getPowerups() {
		synchronized (mPowerups) {
			return mPowerups;
		}
	}
}
