package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.control.ControlInterface;
import hu.bme.mit.asteroid.model.SpaceShip;

/**
 * Egy játékost reprezentáló absztrakt osztály
 */
public abstract class Player implements ControlInterface.Callback {

	public static class State {
		private int mLives;
		private int mPoints;

		public int getLives() {
			return mLives;
		}

		public void setLives(int lives) {
			mLives = lives;
		}

		public int getPoints() {
			return mPoints;
		}

		public void setPoints(int points) {
			mPoints = points;
		}
	}

	private SpaceShip mSpaceShip;
	private State mState;
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

	public State getState() {
		return mState;
	}

	public void setState(State state) {
		mState = state;
	}

	public int getLives() {
		return mState.getLives();
	}

	public void setLives(int lives) {
		mState.setLives(lives);
	}

	public int getPoints() {
		return mState.getPoints();
	}

	public void setPoints(int points) {
		mState.setPoints(points);
	}

	public SpaceShip getSpaceShip() {
		return mSpaceShip;
	}

	public void setSpaceShip(SpaceShip spaceShip) {
		mSpaceShip = spaceShip;
	}

	@Override
	public void control(ControlEvent event) {
		// TODO Vezérlőesemények leképezése az űrhajó irányítására
		switch (event.getType()) {
		case ACCELERATE_START:
			break;
		case ACCELERATE_STOP:
			break;
		case TURN_LEFT_START:
			break;
		case TURN_RIGHT_START:
			break;
		case TURN_STOP:
			break;
		case FIRE_START:
			break;
		case FIRE_STOP:
			break;
		}
	}
}
