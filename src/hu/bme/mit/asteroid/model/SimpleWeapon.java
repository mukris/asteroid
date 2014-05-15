package hu.bme.mit.asteroid.model;

import java.io.Serializable;

/**
 * Egyszerű fegyver...
 */
public class SimpleWeapon extends Weapon implements Serializable {
	private static final long serialVersionUID = -8572824732369348348L;

	private static final float SPEED = 16f;

	public SimpleWeapon() {
		this(new Vector2D());
	}

	public SimpleWeapon(Vector2D position) {
		super(position, new Vector2D((SPEED * MovingSpaceObject.MULTIPLIER), 0.0d));
	}

	@Override
	public SimpleWeapon clone() {
		SimpleWeapon clone = new SimpleWeapon(getPosition());
		clone.mTimeMillisUntilDeath = LIFE_SPAN_MILLIS;
		return clone;
	}

	@Override
	public long getRepeatTime() {
		return 250;
	}
}
