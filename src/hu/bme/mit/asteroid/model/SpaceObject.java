package hu.bme.mit.asteroid.model;

public abstract class SpaceObject {

	private Vector2D mPosition;
	private int mRadius;

	public SpaceObject(Vector2D position) {
		this(position, 0);
	}

	public SpaceObject(Vector2D position, int radius) {
		mPosition = position;
		mRadius = radius;
	}

	public Vector2D getPosition() {
		return mPosition;
	}

	public void setPosition(Vector2D position) {
		mPosition = position;
	}

	public int getRadius() {
		return mRadius;
	}

	public void setRadius(int radius) {
		mRadius = radius;
	}
}
