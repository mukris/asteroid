package hu.bme.mit.asteroid.model;

/**
 * A játékban szereplő űrhajót reprezentáló osztály
 */
public class SpaceShip extends DirectionalMovingSpaceObject {

	private static final int SPACESHIP_SIZE = 50;

	private Vector2D mAcceleration = new Vector2D();
	private Weapon mWeapon;
	private long mTimeMillisUntilVulnerable = 0;
	private long mTimeMillisSinceLastShoot = 0;
	private boolean mIsAccelerating = false;
	private boolean mIsTurningLeft = false;
	private boolean mIsTurningRight = false;
	private boolean mIsFiring = false;

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
	 * Az űrhajó gyorsulásának beállítása
	 * 
	 * @param acceleration
	 *            Az űrhajó új gyorsulása
	 */
	public void setAcceleration(Vector2D acceleration) {
		mAcceleration = acceleration;
	}

	public Weapon getWeapon() {
		return mWeapon;
	}

	public void setWeapon(Weapon weapon) {
		mWeapon = weapon;
	}

	/**
	 * Visszaadja, hogy sebezhető-e az űrhajó. Ha nem sebezhető, akkor képes
	 * áthaladni egy aszteroida fölött.
	 * 
	 * @return True, ha sebezhető, false ha nem
	 */
	public boolean isVulnerable() {
		return mTimeMillisUntilVulnerable == 0;
	}

	/**
	 * Sebezhetetlenség idejének beállítása
	 * 
	 * @param timeMillis
	 *            Az ezredmásodpercben kifejezett időtartamra az űrhajó
	 *            sebezhetetlenné válik
	 */
	public void setUnvulnerableFor(long timeMillis) {
		mTimeMillisUntilVulnerable = timeMillis;
	}

	/**
	 * Gyorsul pillanatnyilag az űrhajó?
	 * 
	 * @return True, ha gyorsul, false ha nem
	 */
	public boolean isAccelerating() {
		return mIsAccelerating;
	}

	/**
	 * Balra fordul pillanatnyilag az űrhajó?
	 * 
	 * @return True, ha fordul, false ha nem
	 */
	public boolean isTurningLeft() {
		return mIsTurningLeft;
	}

	/**
	 * Jobbra fordul pillanatnyilag az űrhajó?
	 * 
	 * @return True, ha fordul, false ha nem
	 */
	public boolean isTurningRight() {
		return mIsTurningRight;
	}

	/**
	 * Tüzel pillanatnyilag az űrhajó?
	 * 
	 * @return True, ha tüzel, false ha nem
	 */
	public boolean isFiring() {
		return mIsFiring;
	}

	/**
	 * Gyorsítás megkezdése
	 */
	public synchronized void accelerateStart() {
		mIsAccelerating = true;
	}

	/**
	 * Gyorsítás befejezése
	 */
	public synchronized void accelerateStop() {
		mIsAccelerating = false;
	}

	/**
	 * Balra fordulás megkezdése
	 */
	public synchronized void rotateLeftStart() {
		mIsTurningLeft = true;
		mIsTurningRight = false;
	}

	/**
	 * Jobbra fordulás megkezdése
	 */
	public synchronized void rotateRightStart() {
		mIsTurningRight = true;
		mIsTurningLeft = false;
	}

	/**
	 * Fordulás befejezése
	 */
	public synchronized void rotateStop() {
		mIsTurningLeft = false;
		mIsTurningRight = false;
	}

	/**
	 * Tüzelés megkezdése
	 */
	public synchronized void fireStart() {
		mIsFiring = true;
	}

	/**
	 * Tüzelés befejezése
	 */
	public synchronized void fireStop() {
		mIsFiring = false;
	}
}
