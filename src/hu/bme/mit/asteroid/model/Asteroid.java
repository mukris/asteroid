package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * A játékban szereplő aszteroidákat reprezentáló osztály
 */
public class Asteroid extends MovingSpaceObject implements Serializable {
	private static final long serialVersionUID = 3948236765179703622L;

	/**
	 * Az aszteroidák lehetséges típusai
	 */
	public enum Type {
		LARGE(75, 2, 3, 3), //
		MEDIUM(50, 4, 5, 2), //
		SMALL(25, 6, 8, 1);

		/**
		 * Asztroida sugara
		 */
		private final int mRadius;

		/**
		 * Specifikációban megadott konstans minimum sebességérték
		 */
		private final int mSpeedMin;

		/**
		 * Specifikációban megadott konstans maximum sebességérték
		 */
		private final int mSpeedMax;

		/**
		 * Asztroida élete, lövés Specifikáció szerint!
		 */
		private final int mMaxHits;

		private Type(int radius, int speedMin, int speedMax, int maxHits) {
			mRadius = radius;
			mSpeedMin = speedMin;
			mSpeedMax = speedMax;
			mMaxHits = maxHits;
		}

		/**
		 * Visszaadja az aszteroidatípus sugarát
		 * 
		 * @return
		 */
		public int getRadius() {
			return mRadius;
		}

		/**
		 * Visszaadja az aszteroidatípus minimális sebességét
		 * 
		 * @return
		 */
		public int getSpeedMin() {
			return mSpeedMin * MULTIPLIER;
		}

		/**
		 * Visszaadja az aszteroidatípus maximális sebességét
		 * 
		 * @return
		 */
		public int getSpeedMax() {
			return mSpeedMax * MULTIPLIER;
		}

		/**
		 * Visszaadja az aszteroidatípus maximális életeinek számát
		 * 
		 * @return
		 */
		public int getMaxHits() {
			return mMaxHits;
		}
	};

	/**
	 * Megadja hogy az aszteroidáknak minimum milyen távolságra kell lenniük a
	 * középponttól a generálásukkor
	 */
	public static final int ASTEROID_MIN_DISTANCE = 100;

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

		setRadius(type.getRadius());
		setHitsLeft(type.getMaxHits());
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
