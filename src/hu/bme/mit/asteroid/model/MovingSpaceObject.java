package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * A játékban szereplő, grafikusan megjelenő mozgó objektumok közös absztrakt
 * őse.
 */
public abstract class MovingSpaceObject extends SpaceObject implements Serializable {
	private static final long serialVersionUID = 1967192605124590747L;

	/**
	 * Az objektum sebessége
	 */
	private Vector2D mSpeed;

	/**
	 * Konstruktor
	 * 
	 * A pozíciót, a sebességet és az objektum sugarát is 0-ra állítja
	 */
	public MovingSpaceObject() {
		this(new Vector2D());
	}

	/**
	 * Konstruktor
	 * 
	 * A sebességet és az objektum sugarát is 0-ra állítja
	 * 
	 * @param position
	 *            Az objektum pozíciója
	 */
	public MovingSpaceObject(Vector2D position) {
		this(position, new Vector2D());
	}

	/**
	 * Konstruktor
	 * 
	 * Az objektum sugarát 0-ra állítja
	 * 
	 * @param position
	 *            Az objektum pozíciója
	 * @param speed
	 *            Az objektum sebessége
	 */
	public MovingSpaceObject(Vector2D position, Vector2D speed) {
		this(position, speed, 0);
	}

	/**
	 * Konstruktor
	 * 
	 * @param position
	 *            Az objektum pozíciója
	 * @param speed
	 *            Az objektum sebessége
	 * @param radius
	 *            Az objektum sugara
	 */
	public MovingSpaceObject(Vector2D position, Vector2D speed, int radius) {
		super(position, radius);
		mSpeed = speed;
	}

	/**
	 * Az objektum sebességének lekérdezése
	 * 
	 * @return Az objektum sebessége
	 */
	public Vector2D getSpeed() {
		return mSpeed;
	}

	/**
	 * Az objektum sebességének beállítása
	 * 
	 * @param speed
	 *            Az objektum új sebessége
	 */
	public void setSpeed(Vector2D speed) {
		mSpeed = speed;
	}
}
