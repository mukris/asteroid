package hu.bme.mit.asteroid.player;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.control.ControlInterface;
import hu.bme.mit.asteroid.model.SpaceShip;

import java.io.Serializable;

/**
 * Egy játékost reprezentáló absztrakt osztály
 */
public abstract class Player implements ControlInterface.Callback {

	/**
	 * A játékos állapotát tároló osztály
	 */
	public static class State implements Serializable {
		private static final long serialVersionUID = 91447011759313932L;

		private int mLives;
		private int mPoints;

		public State(int lives) {
			mLives = lives;
		}

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
		mState = new State(3);
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
		switch (event.getType()) {
		case ACCELERATE_START:
			mSpaceShip.accelerateStart();
			break;
		case ACCELERATE_STOP:
			mSpaceShip.accelerateStop();
			break;
		case TURN_LEFT_START:
			mSpaceShip.rotateLeftStart();
			break;
		case TURN_RIGHT_START:
			mSpaceShip.rotateRightStart();
			break;
		case TURN_STOP:
			mSpaceShip.rotateStop();
			break;
		case FIRE_START:
			mSpaceShip.fireStart();
			break;
		case FIRE_STOP:
			mSpaceShip.fireStop();
			break;

		default:
			break;
		}
	}
}
