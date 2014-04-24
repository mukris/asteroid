package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * Egyszerű fegyver...
 */
public class SimpleWeapon extends Weapon implements Serializable {
	private static final long serialVersionUID = -5555076858422220086L;

	private static final float SPEED = 16f;

	public SimpleWeapon() {
		this(new Vector2D());
	}

	public SimpleWeapon(Vector2D position) {
		super(position, new Vector2D(SPEED, 0));
	}

	@Override
	public SimpleWeapon clone() {
		SimpleWeapon clone = new SimpleWeapon(getPosition());
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
		return 250;
	}
}
