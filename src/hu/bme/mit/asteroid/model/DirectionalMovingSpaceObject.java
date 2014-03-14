package hu.bme.mit.asteroid.model;

/**
 * A játékban szereplő, grafikusan megjelenő mozgó és elfordulásra képes
 * objektumok közös absztrakt őse.
 */
public abstract class DirectionalMovingSpaceObject extends MovingSpaceObject {

	/**
	 * <p>
	 * Az objektum elfordulási iránya fokban kifejezve
	 * </p>
	 * <p>
	 * Érvényes értékek: 0-359
	 * </p>
	 * <p>
	 * A 0 érték a felfele irányt jelenti, a számok az óramutató járásával
	 * megegyező irányban nőnek
	 * </p>
	 */
	private int mDirection;

	/**
	 * Konstruktor
	 * 
	 * @param position
	 *            Az objektum pozíciója
	 * @param speed
	 *            Az objektum sebessége
	 * @param direction
	 *            Az objektum elfordulási iránya
	 * @param radius
	 *            Az objektum sugara
	 */
	public DirectionalMovingSpaceObject(Vector2D position, Vector2D speed, int direction, int radius) {
		super(position, speed, radius);
		mDirection = direction;
	}

	/**
	 * Az objektum elfordulási irányának lekérdezése
	 * 
	 * @return Az objektum elfordulási iránya
	 */
	public int getDirection() {
		return mDirection;
	}

	/**
	 * Az objektum elfordulási irányának beállítása
	 * 
	 * @param direction
	 *            Az objektum új elfordulási iránya
	 */
	public void setDirection(int direction) {
		mDirection = direction % 360;
	}

}
