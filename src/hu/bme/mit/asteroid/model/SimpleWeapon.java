package hu.bme.mit.asteroid.model;

/**
 * Egyszerű fegyver...
 */
public class SimpleWeapon extends Weapon {

	public SimpleWeapon(Vector2D position, Vector2D speed) {
		super(position, speed);
	}

	@Override
	public SimpleWeapon clone() {
		SimpleWeapon clone = new SimpleWeapon(getPosition(), getSpeed());
		clone.mTimeMillisUntilDeath = LIFE_SPAN_MILLIS;
		return clone;
	}

	@Override
	public boolean isAlive(long currentTime) {
		// TODO lövedék élettartamának meghatározása (időkülönbség, ill sebesség
		// alapján)
		return true;
	}

	@Override
	public long getRepeatTime() {
		// FIXME pontos lövés ismétlési időköz beállítása
		return 0;
	}
}
