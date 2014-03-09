package hu.bme.mit.asteroid.model;

public class Vector2D {

	private float mX;
	private float mY;

	public Vector2D() {
		mX = mY = 0;
	}

	public Vector2D(float x, float y) {
		mX = x;
		mY = y;
	}

	public Vector2D(float length, int direction) {
		// TODO
	}

	public float getX() {
		return mX;
	}

	public void setX(float x) {
		mX = x;
	}

	public float getY() {
		return mY;
	}

	public void setY(float y) {
		mY = y;
	}

	public Vector2D add(Vector2D vector) {
		// TODO
		return this;
	}
	
	public int getDirection() {
		// FIXME
		return 0;
	}

	public float getLength() {
		// FIXME
		return 0;
	}
}
