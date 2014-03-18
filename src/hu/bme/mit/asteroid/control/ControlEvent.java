package hu.bme.mit.asteroid.control;

public class ControlEvent {

	public enum Type {
		ACCELERATE_START, ACCELERATE_STOP, TURN_LEFT_START, TURN_RIGHT_START, TURN_STOP, FIRE_START, FIRE_STOP
	}

	private final Type mType;

	public ControlEvent(Type type) {
		mType = type;
	}

	public Type getType() {
		return mType;
	}
}
