package hu.bme.mit.asteroid.model;

/**
 * A játékban szereplő, grafikusan megjelenő objektumok közös absztrakt őse.
 */
public abstract class SpaceObject {

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
}
