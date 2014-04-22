package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * Két dimenziós vektor
 */
public class Vector2D implements Cloneable, Serializable {
	private static final long serialVersionUID = 2869327434602565264L;
	
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

	@Override
	public Vector2D clone() {
		return new Vector2D(mX, mY);
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

	public Vector2D multiply(float multiplier) {
		setLength(getLength() * multiplier);
		return this;
	}

	public int getDirection() {
		return (int) (Math.atan2(mY, mX) * 180 / Math.PI);
	}

	public void setDirection(int direction) {
		mX = (float) (getLength() * Math.cos(direction * Math.PI / 180));
		mY = (float) (getLength() * Math.sin(direction * Math.PI / 180));
	}

	public float getLength() {
		return (float) Math.sqrt(mX * mX + mY * mY);
	}

	public void setLength(float length) {
		mX = (float) (length * Math.cos(getDirection() * Math.PI / 180));
		mY = (float) (length * Math.sin(getDirection() * Math.PI / 180));
	}
}
