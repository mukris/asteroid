package hu.bme.mit.asteroid.exceptions;

/**
 * Nem létező pályát jelző exception
 */
@SuppressWarnings("serial")
public class LevelNotExistsException extends LevelNotLoadableException {
	@Override
	public String getMessage() {
		return new String("A pálya nem létezik.");
	}
}
