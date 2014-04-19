package hu.bme.mit.asteroid.exceptions;

/**
 * Feloldatlan pályát jelző exception
 */
@SuppressWarnings("serial")
public class LevelNotUnlockedException extends LevelNotLoadableException {
	@Override
	public String getMessage() {
		return new String("A pálya nincs feloldva.");
	}
}
