package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * A j√°t√©kban szerepl≈ë aszteroid√°kat reprezent√°l√≥ oszt√°ly
 */
public class Asteroid extends MovingSpaceObject implements Serializable {
	private static final long serialVersionUID = 8613471195440058251L;

	/**
	 * Az aszteroid√°k lehets√©ges t√≠pusai
	 */
	public enum Type {
		LARGE, MEDIUM, SMALL
	};

	private static final int ASTEROID_SIZE_LARGE = 100;
	private static final int ASTEROID_SIZE_MEDIUM = 50;
	private static final int ASTEROID_SIZE_SMALL = 25;
	

	/**
	 * Specifik·ciÛban megadott konstans sebessÈgÈrtÈkek
	 * remÈlem itt jÛ helye lesz, ha nem rakj·tok ·t
	 */
	public static final float ASTEROID_SPEED_LARGE_MIN = 2;
	public static final float ASTEROID_SPEED_LARGE_MAX = 3;
	public static final float ASTEROID_SPEED_MEDIUM_MIN = 4;
	public static final float ASTEROID_SPEED_MEDIUM_MAX = 5;
	public static final float ASTEROID_SPEED_SMALL_MIN = 6;
	public static final float ASTEROID_SPEED_SMALL_MAX = 8;
	
	/**
	 * Megadja hogy az aszteroid·knak minimum milyen t·vols·gra kell lenni¸k a kˆzÈpponttÛl a gener·l·sukkor
	 */
	public static final int ASTEROID_MIN_DISTANCE = 30;
	
	/**
	 * Asztroida Èlete, lˆvÈs
	 * Specifik·ciÛ szerint!
	 */
	public static final int ASTEROID_MAXHITS_LARGE = 3;
	public static final int ASTEROID_MAXHITS_MEDIUM = 2;
	public static final int ASTEROID_MAXHITS_SMALL = 1;


	private Type mType;

	public Asteroid() {
		this(Type.LARGE, new Vector2D(), new Vector2D());
	}

	/**
	 * Konstruktor
	 * 
	 * @param type
	 *            Az aszteroida t√≠pusa
	 * @param position
	 *            Az aszteroida poz√≠ci√≥ja
	 * @param speed
	 *            Az aszteroida sebess√©ge
	 */
	public Asteroid(Type type, Vector2D position, Vector2D speed) {
		super(position, speed);

		int radius = 0;
		int maxhits = 0;

		switch (type) {
		case LARGE:
			radius = ASTEROID_SIZE_LARGE;
			maxhits = ASTEROID_MAXHITS_LARGE;
			break;
		case MEDIUM:
			radius = ASTEROID_SIZE_MEDIUM;
			maxhits = ASTEROID_MAXHITS_MEDIUM;
			break;
		case SMALL:
			radius = ASTEROID_SIZE_SMALL;
			maxhits = ASTEROID_MAXHITS_SMALL;
			break;
		}

		setRadius(radius);
		setHitsLeft(maxhits);
		mType = type;
	}

	/**
	 * Az aszteroida t√≠pus√°nak lek√©rdez√©se
	 * 
	 * @return Az aszteroida t√≠pusa
	 */
	public Type getType() {
		return mType;
	}
}
