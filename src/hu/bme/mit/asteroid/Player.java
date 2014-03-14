package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlInterface;
import hu.bme.mit.asteroid.model.SpaceShip;

/**
 * Egy játékost reprezentáló absztrakt osztály
 */
public abstract class Player implements ControlInterface.Callback {

	private SpaceShip mSpaceShip;
	private int mLives;
	private int mPoints;
	private ControlInterface mControlInterface;

	/**
	 * Konstruktor
	 * 
	 * @param controlInterface
	 *            A felhasználó vezérlő utasításait eseményekké leképező
	 *            objektum
	 */
	public Player(ControlInterface controlInterface) {
		mControlInterface = controlInterface;
		if (mControlInterface != null) {
			mControlInterface.setCallback(this);
		}
	}

	@Override
	public void onAccelerate() {
		if (mSpaceShip != null) {
			mSpaceShip.accelerate();
		}
	}

	@Override
	public void onTurnLeft() {
		if (mSpaceShip != null) {
			mSpaceShip.rotateLeft();
		}
	}

	@Override
	public void onTurnRight() {
		if (mSpaceShip != null) {
			mSpaceShip.rotateRight();
		}
	}

	@Override
	public void onFire() {
		if (mSpaceShip != null) {
			mSpaceShip.fire();
		}
	}
}
