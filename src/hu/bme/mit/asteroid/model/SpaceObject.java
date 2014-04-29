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
	 * Az objektum (aszteroida) �lete
	 */
	private int mHitsLeft;

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
	 * Az objektum (aszteroida) h�tralev� �let�nek �ll�t�sa
	 * @param hitsleft
	 */
	public void setHitsLeft(int hitsleft) {
		mHitsLeft = hitsleft;
	}

	/**
	 * Ellenőrzi, hogy a vizsgált objektum ütközik-e a paraméterül kapott másik
	 * objektummal.
	 * 
	 * @param other
	 *            Másik objektum
	 * @return True, ha igen, false, ha nem.
	 */
	public boolean checkCollision(SpaceObject other) {
		return (Vector2D.getDistance(mPosition, other.mPosition) < (mRadius + other.mRadius));
	}
}
