package hu.bme.mit.asteroid.exceptions;

public class LevelNotUnlockedException extends LevelNotLoadableException {
	@Override
	public String getMessage() {
		return new String("A pálya nincs feloldva.");
	}
}
