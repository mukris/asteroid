package hu.bme.mit.asteroid.model;

/**
 * A játékban szereplő aszteroidákat reprezentáló osztály
 */
public class Asteroid extends MovingSpaceObject {

	/**
	 * Az aszteroidák lehetséges típusai
	 */
	public enum Type {
		LARGE, MEDIUM, SMALL
	};

	private static final int ASTEROID_SIZE_LARGE = 100;
	private static final int ASTEROID_SIZE_MEDIUM = 50;
	private static final int ASTEROID_SIZE_SMALL = 25;

	private Type mType;

	/**
	 * Konstruktor
	 * 
	 * @param type
	 *            Az aszteroida típusa
	 * @param position
	 *            Az aszteroida pozíciója
	 * @param speed
	 *            Az aszteroida sebessége
	 */
	public Asteroid(Type type, Vector2D position, Vector2D speed) {
		super(position, speed);

		int radius = 0;

		switch (type) {
		case LARGE:
			radius = ASTEROID_SIZE_LARGE;
			break;
		case MEDIUM:
			radius = ASTEROID_SIZE_MEDIUM;
			break;
		case SMALL:
			radius = ASTEROID_SIZE_SMALL;
			break;
		}

		setRadius(radius);
		mType = type;
	}

	/**
	 * Az aszteroida típusának lekérdezése
	 * 
	 * @return Az aszteroida típusa
	 */
	public Type getType() {
		return mType;
	}
}
