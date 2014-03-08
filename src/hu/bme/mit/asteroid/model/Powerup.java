package hu.bme.mit.asteroid.model;

public class Powerup extends SpaceObject {

	private final static int POWERUP_SIZE = 20;

	public Powerup(Vector2D position) {
		super(position, POWERUP_SIZE);
	}
}
