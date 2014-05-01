package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * Két dimenziós vektor
 */
public class Vector2D implements Cloneable, Serializable {
	private static final long serialVersionUID = -1963281493081005939L;

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

	public Vector2D limit(float maxLength) {
		if (getLength() > maxLength) {
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

	/**
	 * Véletlen pozíció generálása a játékmezőn a középponttól bizonyos
	 * távolságon kívül
	 * 
	 * @param maxX
	 *            A vektor maximális X koordinátája
	 * @param maxY
	 *            A vektor maximális Y koordinátája
	 * @param minDistance
	 *            Minimális távolság a középponttól
	 * @return Véletlen vektor
	 */
	public static Vector2D generateRandomPosition(int maxX, int maxY, float minDistance) {
		if (maxX < 1 || maxY < 1) {
			throw new IllegalArgumentException();
		}

		final Vector2D centerVector = new Vector2D(maxX / 2, maxY / 2);
		Vector2D randomVector = new Vector2D();

		do {
			randomVector.setX((float) (Math.random() * maxX));
			randomVector.setY((float) (Math.random() * maxY));
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

	/**
	 * Véletlen irányú, megadott korlátok között véletlen hosszúságú vektort
	 * generál
	 * 
	 * @param minLength
	 *            Minimális hossz
	 * @param maxLength
	 *            Maximális hossz
	 * @return Véletlen vektor
	 */
	public static Vector2D generateRandom(float minLength, float maxLength) {
		return generateRandomDirection(generateRandomLength(minLength, maxLength));
	}
}
