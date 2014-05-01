package hu.bme.mit.asteroid.model;

import hu.bme.mit.asteroid.AsteroidGame;

import java.io.Serializable;

/**
 * Két dimenziós vektor
 */
public class Vector2D implements Cloneable, Serializable {
	private static final long serialVersionUID = 3385898139144094442L;

	private float mX;
	private float mY;

	public Vector2D() {
		mX = mY = 0;
	}

	public Vector2D(float x, float y) {
		mX = x;
		mY = y;
	}

	public Vector2D(float length, double direction) {
		mX = (float) (length * Math.cos(direction));
		mY = (float) (length * Math.sin(direction));
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

	public Vector2D inRange(int x, int y) {
		if (mX > x) {
			mX -= x;
		} else if (mX < 0) {
			mX += x;
		}
		if (mY > y) {
			mY -= y;
		} else if (mY < 0) {
			mY += y;
		}
		return this;
	}
	
	public Vector2D limit(float maxLength){
		if(getLength() > maxLength) {
			setLength(maxLength);
		}
		return this;
	}

	public Vector2D multiply(float multiplier) {
		setLength(getLength() * multiplier);
		return this;
	}

	public double getDirection() {
		return Math.atan2(mY, mX);
	}

	public void setDirection(double direction) {
		final float length = getLength();
		mX = (float) (length * Math.cos(direction));
		mY = (float) (length * Math.sin(direction));
	}

	public float getLength() {
		return (float) Math.sqrt(mX * mX + mY * mY);
	}

	public void setLength(float length) {
		final double direction = getDirection();
		mX = (float) (length * Math.cos(direction));
		mY = (float) (length * Math.sin(direction));
	}

	public float getDistance(Vector2D other) {
		return getDistance(this, other);
	}

	public static float getDistance(Vector2D v1, Vector2D v2) {
		return (float) Math.sqrt((v1.mX - v2.mX) * (v1.mX - v2.mX) + (v1.mY - v2.mY) * (v1.mY - v2.mY));
	}

	// véletlen koordinátát generál a megadott térrészben
	// int mert a képpontok intek
	// 4 térrészben generál random pontot, a végén ebből a 4 térrészből választ
	// egyet
	// nincs jobb ötletem

	public static Vector2D generateRandomPosition(float minDistance) {
		float maxX = AsteroidGame.WINDOW_SIZE_X;
		float maxY = AsteroidGame.WINDOW_SIZE_Y;
		Vector2D randomVector = new Vector2D();
		Vector2D centerVector = new Vector2D (AsteroidGame.WINDOW_SIZE_X / 2 , AsteroidGame.WINDOW_SIZE_Y / 2);

		do {
			randomVector.setX((float)(Math.random() * maxX));
			randomVector.setY((float)(Math.random() * maxY));
		} while (getDistance(randomVector, centerVector) < minDistance);

		return randomVector;
	}

	/**
	 * Véletlen irányt generál adott hosszúsággal
	 * 
	 * @param length
	 *            A vektor kívánt hossza
	 * @return Véletlen irányú vektor
	 */
	public static Vector2D generateRandomDirection(float length) {
		return new Vector2D(length, (float) (Math.random() * 2 * Math.PI));
	}

	/**
	 * Véletlen hosszat generál az adott korlátok között
	 * 
	 * @param minLength
	 *            Minimális hossz
	 * @param maxLength
	 *            Maximális hossz
	 * @return Véletlen hosszúság érték
	 */
	public static float generateRandomLength(float minLength, float maxLength) {
		return (float) (minLength + Math.random() * (maxLength - minLength));
	}
	
}
