package hu.bme.mit.asteroid.model;

/**
 * A játékban szereplő fegyvereket reprezentáló absztrakt osztály
 */
public abstract class Weapon extends DirectionalMovingSpaceObject {
	
	private static final int WEAPON_SIZE = 10;

	public Weapon(Vector2D position, Vector2D speed) {
		super(position, speed, speed.getDirection(), WEAPON_SIZE);
	}

}
