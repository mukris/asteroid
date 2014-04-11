package hu.bme.mit.asteroid.model;

/**
 * A játékban szereplő űrhajót reprezentáló osztály
 */
public class SpaceShip extends DirectionalMovingSpaceObject {

	private static final int SPACESHIP_SIZE = 50;

	private Vector2D mAcceleration = new Vector2D();
	private Weapon mWeapon;
	private long timeMillisUntilVulnerable = 0;

	/**
	 * Konstruktor
	 * 
	 * @param position
	 *            Az űrhajó pozíciója
	 * @param direction
	 *            Az űrhajó elfordulási iránya
	 */
	public SpaceShip(Vector2D position, int direction) {
		super(position, new Vector2D(), direction, SPACESHIP_SIZE);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Az űrhajó gyorsulásának lekérdezése
	 * 
	 * @return Az űrhajó gyorsulása
	 */
	public Vector2D getAcceleration() {
		return mAcceleration;
	}

	/**
	 * Gyorsul pillanatnyilag az űrhajó?
	 * 
	 * @return True, ha gyorsul, false ha nem
	 */
	public boolean isAccelerating() {
		return (mAcceleration.getLength() > 0.001) ? true : false;
	}

	/**
	 * Az űrhajó gyorsulásának beállítása
	 * 
	 * @param acceleration
	 *            Az űrhajó új gyorsulása
	 */
	public void setAcceleration(Vector2D acceleration) {
		mAcceleration = acceleration;
	}
	
	public void accelerateStart() {
		// TODO
	}

	public void accelerateStop() {
		// TODO
	}

	public void rotateLeftStart() {
		// TODO
	}
	
	public void rotateRightStart() {
		// TODO
	}

	public void rotateStop() {
		// TODO
	}
	
	public void fireStart() {
		// TODO
	}

	public void fireStop() {
		// TODO
	}
}
