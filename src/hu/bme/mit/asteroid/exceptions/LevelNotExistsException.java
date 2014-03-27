package hu.bme.mit.asteroid.exceptions;

public class LevelNotExistsException extends LevelNotLoadableException {
	@Override
	public String getMessage() {
		return new String("A pálya nem létezik.");
	}
}
