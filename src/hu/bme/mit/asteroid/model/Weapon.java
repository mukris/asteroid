package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * A játékban szereplő fegyvereket reprezentáló absztrakt osztály
 */
public abstract class Weapon extends DirectionalMovingSpaceObject implements Cloneable, Serializable {
	private static final long serialVersionUID = -1604474174764776496L;
	
	private static final int WEAPON_SIZE = 10;
	public static final int LIFE_SPAN_MILLIS = 5000;
	protected long mTimeMillisUntilDeath = 0;

	public Weapon() {
		this(new Vector2D(), new Vector2D());
	}

	public Weapon(Vector2D position, Vector2D speed) {
		super(position, speed, speed.getDirection(), WEAPON_SIZE);
	}

	@Override
	public abstract Weapon clone();

	/**
	 * Visszaadja, hogy a kérdéses időpillanatban a lövedék még "éles"-e. Ez
	 * segít a lövedék maximális élettartamát meghatározni, hogy egy kilőtt
	 * lövedék ne keringjen a végtelenségig.
	 * 
	 * @return True, ha még aktív, false ha nem.
	 */
	public boolean isAlive() {
		return mTimeMillisUntilDeath > 0;
	}

	/**
	 * Visszaadja a fegyver ismétlési idejét ezredmásodpercben. Folyamatos
	 * tüzelés mellett az űrhajó ilyen gyakorisággal lő ki egy-egy újabb
	 * lövedéket.
	 * 
	 * @return A fegyver ismétlési ideje ezredmásodpercben
	 */
	public abstract long getRepeatTime();

	/**
	 * A hátralévő élet időszámlálójának csökkentése
	 * 
	 * @param timeDelta
	 *            Az eltelt idő ezredmásodpercben
	 */
	public void decreaseTimeUntilDeath(long timeDelta) {
		mTimeMillisUntilDeath -= timeDelta;
	}

	@Override
	public void setDirection(double direction) {
		super.setDirection(direction);
		getSpeed().setDirection(direction);
	}
}
