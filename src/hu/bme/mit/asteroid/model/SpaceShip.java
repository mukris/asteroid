package hu.bme.mit.asteroid.model;

public class SpaceShip extends DirectionalMovingSpaceObject {
	
	private static final int SPACESHIP_SIZE = 50;

	private Vector2D mAcceleration = new Vector2D();
	private Weapon mWeapon;

	public SpaceShip(Vector2D position, int direction) {
		super(position, new Vector2D(), direction, SPACESHIP_SIZE);
		// TODO Auto-generated constructor stub
	}

	public Vector2D getAcceleration() {
		return mAcceleration;
	}

	public void setAcceleration(Vector2D acceleration) {
		mAcceleration = acceleration;
	}

	public void accelerate() {
		// TODO
	}

	public void rotateLeft() {
		// TODO
	}

	public void rotateRight() {
		// TODO
	}

	public void fire() {
		// TODO
	}
}
