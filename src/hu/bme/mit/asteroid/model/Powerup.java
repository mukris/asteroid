package hu.bme.mit.asteroid.model;

/**
 * A játékban szereplő bónuszokat reprezentáló osztály
 */
public class Powerup extends SpaceObject {

	private final static int POWERUP_SIZE = 20;

	public Powerup(Vector2D position) {
		super(position, POWERUP_SIZE);
	}
}
