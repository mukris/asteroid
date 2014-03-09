package hu.bme.mit.asteroid;

public abstract class ControlInterface {
	public static interface Callback {
		public void onAccelerate();
		public void onTurnLeft();
		public void onTurnRight();
		public void onFire();
	}
	
	private Callback mCallback;

	public void setCallback(Callback callback) {
		mCallback = callback;
	}
}
