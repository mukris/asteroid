package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * A játékban szereplő aszteroidákat reprezentáló osztály
 */
public class Asteroid extends MovingSpaceObject implements Serializable {
	private static final long serialVersionUID = -1228942266636338555L;

	/**
	 * Az aszteroidák lehetséges típusai
	 */
	public enum Type {
		LARGE, MEDIUM, SMALL
	};

	public static final int RADIUS_LARGE = 75;
	public static final int RADIUS_MEDIUM = 50;
	public static final int RADIUS_SMALL = 25;

	/**
	 * Specifikációban megadott konstans sebességértékek
	 */
	public static final float ASTEROID_SPEED_LARGE_MIN = 2;
	public static final float ASTEROID_SPEED_LARGE_MAX = 3;
	public static final float ASTEROID_SPEED_MEDIUM_MIN = 4;
	public static final float ASTEROID_SPEED_MEDIUM_MAX = 5;
	public static final float ASTEROID_SPEED_SMALL_MIN = 6;
	public static final float ASTEROID_SPEED_SMALL_MAX = 8;

	/**
	 * Megadja hogy az aszteroidáknak minimum milyen távolságra kell lenniük a
	 * középponttól a generálásukkor
	 */
	public static final int ASTEROID_MIN_DISTANCE = 100;

	/**
	 * Asztroida élete, lövés Specifikáció szerint!
	 */
	public static final int ASTEROID_MAXHITS_LARGE = 3;
	public static final int ASTEROID_MAXHITS_MEDIUM = 2;
	public static final int ASTEROID_MAXHITS_SMALL = 1;

	/**
	 * Az aszteroida hátralévő életeinek száma
	 */
	private int mHitsLeft;

	private Type mType;

	public Asteroid() {
		this(Type.LARGE, new Vector2D(), new Vector2D());
	}

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
		int maxhits = 0;

		switch (type) {
		case LARGE:
			radius = RADIUS_LARGE;
			maxhits = ASTEROID_MAXHITS_LARGE;
			break;
		case MEDIUM:
			radius = RADIUS_MEDIUM;
			maxhits = ASTEROID_MAXHITS_MEDIUM;
			break;
		case SMALL:
			radius = RADIUS_SMALL;
			maxhits = ASTEROID_MAXHITS_SMALL;
			break;
		}

		setRadius(radius);
		setHitsLeft(maxhits);
		mType = type;
	}

	/**
	 * Az aszteroida hátralevő életének beállítása
	 * 
	 * @param hitsleft
	 */
	public void setHitsLeft(int hitsleft) {
		mHitsLeft = hitsleft;
	}

	/**
	 * Visszaadja az aszteroida hátralévő életének számát
	 * 
	 * @return Az aszteroida hátralévő életének száma
	 */
	public int getHitsLeft() {
		return mHitsLeft;
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
