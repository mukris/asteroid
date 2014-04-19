package hu.bme.mit.asteroid.control;

import java.io.Serializable;

/**
 * Különböző vezérlőutasításokat, eseményeket reprezentáló osztály
 */
public class ControlEvent implements Serializable {
	private static final long serialVersionUID = -4401770362499254528L;

	/**
	 * A {@link ControlEvent}-ek lehetséges típusait reprezentáló enum
	 */
	public enum Type {
		ACCELERATE_START, ACCELERATE_STOP, TURN_LEFT_START, TURN_RIGHT_START, TURN_STOP, FIRE_START, FIRE_STOP, INVERT_PAUSE
	}

	private final Type mType;

	public ControlEvent(Type type) {
		mType = type;
	}

	public Type getType() {
		return mType;
	}
}
