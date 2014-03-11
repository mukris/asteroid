package hu.bme.mit.asteroid.control;

public abstract class ControlInterface {
	public static interface Callback {
		public void onAccelerate();
		public void onTurnLeft();
		public void onTurnRight();
		public void onFire();
	}
	
	protected Callback mCallback;

	public void setCallback(Callback callback) {
		mCallback = callback;
	}
}
