package hu.bme.mit.asteroid.model;

/**
 * Két dimenziós vektor
 */
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
		mX = (float) (length * Math.cos(direction * Math.PI / 180));
		mY = (float) (length * Math.sin(direction * Math.PI / 180));
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
		mX += vector.mX;
		mY += vector.mY;
		return this;
	}

	public int getDirection() {
		return (int) (Math.atan2(mY, mX) * 180 / Math.PI);
	}

	public float getLength() {
		return (float) Math.sqrt(mX * mX + mY * mY);
	}
}
