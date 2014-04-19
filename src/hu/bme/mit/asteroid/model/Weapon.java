package hu.bme.mit.asteroid.model;

/**
 * A játékban szereplő fegyvereket reprezentáló absztrakt osztály
 */
public abstract class Weapon extends DirectionalMovingSpaceObject implements Cloneable {

	private static final int WEAPON_SIZE = 10;
	protected static final int LIFE_SPAN_MILLIS = 5000;
	protected long mTimeMillisUntilDeath = 0;

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
	 * @param currentTime
	 *            Az aktuális idő ms-ban
	 * @return True, ha még aktív, false ha nem.
	 */
	public abstract boolean isAlive(long currentTime);
	
	/**
	 * Visszaadja a fegyver ismétlési idejét ezredmásodpercben. Folyamatos
	 * tüzelés mellett az űrhajó ilyen gyakorisággal lő ki egy-egy újabb
	 * lövedéket.
	 * 
	 * @return A fegyver ismétlési ideje ezredmásodpercben
	 */
	public abstract long getRepeatTime();
}
