package hu.bme.mit.asteroid.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A játékban szereplő űrhajót reprezentáló osztály
 */
public class SpaceShip extends DirectionalMovingSpaceObject implements Serializable {
	private static final long serialVersionUID = -3082685416683514085L;

	private static final int SPACESHIP_SIZE = 50;
	public static final int DEFAULT_UNVULNERABILITY_TIME = 5000;

	private Vector2D mAcceleration = new Vector2D();
	private Weapon mWeapon;
	private List<Weapon> mWeapons;
	private long mTimeMillisUntilVulnerable = 0;
	private long mTimeMillisSinceLastShoot = 0;
	private boolean mIsAccelerating = false;
	private boolean mIsTurningLeft = false;
	private boolean mIsTurningRight = false;
	private boolean mIsFiring = false;

	/**
	 * Konstruktor
	 * 
	 * Minden értéket 0-ra állít
	 */
	public SpaceShip() {
		this(new Vector2D(), 0, null);
	}

	/**
	 * Konstruktor
	 * 
	 * @param position
	 *            Az űrhajó pozíciója
	 * @param direction
	 *            Az űrhajó elfordulási iránya
	 */
	public SpaceShip(Vector2D position, double direction, Weapon weapon) {
		super(position, new Vector2D(), direction, SPACESHIP_SIZE);
		mWeapons = new ArrayList<>();
		mWeapon = weapon;
		mWeapon.setPosition(position);
		mWeapon.setDirection(direction);
	}

	/**
	 * Tüzelés kezelése. A fizikai számítások ciklusában hívandó, kezeli a
	 * fegyver ismétlési idejét.
	 * 
	 * @param timeDelta
	 *            Az előző hívás óta eltelt idő ezredmásodpercben
	 */
	public void handleFiring(long timeDelta) {
		long repeatTime = mWeapon.getRepeatTime();
		if (isFiring() && mTimeMillisSinceLastShoot + timeDelta >= repeatTime) {
			Weapon newWeapon = mWeapon.clone();
			newWeapon.setDirection(getDirection());
			newWeapon.setPosition(getPosition().clone());
			synchronized (mWeapons) {
				mWeapons.add(newWeapon);
			}
			mTimeMillisSinceLastShoot %= repeatTime;
		}
		mTimeMillisSinceLastShoot += timeDelta;
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

	public List<Weapon> getWeapons() {
		synchronized (mWeapons) {
			return mWeapons;
		}
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
	 * Visszaadja, hogy mennyi ideig sebezhetetlen még az űrhajó
	 * 
	 * @return A sebezhetetlenségből hátralévő idő ezredmásodpercben
	 */
	public long getUnvulnerableFor() {
		return mTimeMillisUntilVulnerable;
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
