package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * A játékban szereplő bónuszokat reprezentáló osztály
 */
public class Powerup extends SpaceObject implements Serializable {
	private static final long serialVersionUID = -4998080445519246160L;

	private final static int POWERUP_SIZE = 20;

	public Powerup() {
		this(new Vector2D());
	}

	public Powerup(Vector2D position) {
		super(position, POWERUP_SIZE);
	}

	public void doAction(SpaceShip spaceShip) {

	}
}
