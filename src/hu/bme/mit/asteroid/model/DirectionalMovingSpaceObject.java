package hu.bme.mit.asteroid.model;

public abstract class DirectionalMovingSpaceObject extends MovingSpaceObject {

	/**
	 * Direction in degrees
	 * 
	 * Valid values: 0-359
	 * 
	 * 0 means up, turning clockwise
	 */
	private int mDirection;

	public DirectionalMovingSpaceObject(Vector2D position, Vector2D speed,
			int direction, int radius) {
		super(position, speed, radius);
		mDirection = direction;
	}

	public int getDirection() {
		return mDirection;
	}

	public void setDirection(int direction) {
		mDirection = direction % 360;
	}

}
