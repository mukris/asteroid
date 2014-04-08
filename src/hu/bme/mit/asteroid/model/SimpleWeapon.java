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
		return new SimpleWeapon(getPosition(), getSpeed());
	}

	@Override
	public boolean isAlive(long currentTime) {
		// TODO lövedék élettartamának meghatározása (időkülönbség, ill sebesség alapján)
		return true;
	}
}
