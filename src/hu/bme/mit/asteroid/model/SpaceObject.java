package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * A játékban szereplő, grafikusan megjelenő objektumok közös absztrakt őse.
 */
public abstract class SpaceObject implements Serializable {
	private static final long serialVersionUID = -3831949381019717002L;

	/**
	 * Az objektum pozíciója
	 */
	private Vector2D mPosition;

	/**
	 * Az objektum sugara
	 */
	private int mRadius;

	/**
	 * Konstruktor
	 * 
	 * A pozíciót és az objektum sugarát 0-ra állítja
	 */
	public SpaceObject() {
		this(new Vector2D());
	}

	/**
	 * Konstruktor
	 * 
	 * Az objektum sugarát 0-ra állítja
	 * 
	 * @param position
	 *            Az objektum pozíciója
	 */
	public SpaceObject(Vector2D position) {
		this(position, 0);
	}

	/**
	 * Konstruktor
	 * 
	 * @param position
	 *            Az objektum pozíciója
	 * @param radius
	 *            Az objektum sugara
	 */
	public SpaceObject(Vector2D position, int radius) {
		mPosition = position;
		mRadius = radius;
	}

	/**
	 * Az objektum pozíciójának lekérdezése
	 * 
	 * @return Az objektum pozíciója
	 */
	public Vector2D getPosition() {
		return mPosition;
	}

	/**
	 * Az objektum pozíciójának beállítása
	 * 
	 * @param position
	 *            Az objektum új pozíciója
	 */
	public void setPosition(Vector2D position) {
		mPosition = position;
	}

	/**
	 * Az objektum sugarának lekérdezése
	 * 
	 * @return Az objektum sugara
	 */
	public int getRadius() {
		return mRadius;
	}

	/**
	 * Az objektum sugarának beállítása
	 * 
	 * @param radius
	 *            Az objektum új sugara
	 */
	public void setRadius(int radius) {
		mRadius = radius;
	}

	/**
	 * Ellenőrzi, hogy a vizsgált objektum ütközik-e a paraméterül kapott másik
	 * objektummal.
	 * 
	 * @param other
	 *            Másik objektum
	 * @param width
	 *            A játékmező szélessége
	 * @param height
	 *            A játékmező magassága
	 * @return True, ha igen, false, ha nem.
	 */
	public boolean checkCollision(SpaceObject other, int width, int height) {
		return (mPosition.getDistanceInRange(other.mPosition, width, height) < (mRadius + other.mRadius));
	}
}
