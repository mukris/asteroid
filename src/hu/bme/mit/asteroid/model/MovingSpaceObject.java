package hu.bme.mit.asteroid.model;

public abstract class MovingSpaceObject extends SpaceObject {

	private Vector2D mSpeed;

	public MovingSpaceObject(Vector2D position) {
		this(position, new Vector2D());
	}

	public MovingSpaceObject(Vector2D position, Vector2D speed) {
		this(position, speed, 0);
	}
	
	public MovingSpaceObject(Vector2D position, Vector2D speed, int radius) {
		super(position, radius);
		mSpeed = speed;
	}

	public Vector2D getSpeed() {
		return mSpeed;
	}

	public void setSpeed(Vector2D speed) {
		mSpeed = speed;
	}
}
