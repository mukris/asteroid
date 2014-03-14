package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.Weapon;

import java.util.ArrayList;

/**
 * A játék midenkori aktuális állapotát tároló osztály
 */
public class GameState {

	private ArrayList<Asteroid> mAsteroids;
	private ArrayList<Weapon> mWeapons;
}
