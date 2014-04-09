package hu.bme.mit.asteroid.control;

import hu.bme.mit.asteroid.control.ControlEvent.Type;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A billentyűzet nyíl, illetve szóköz billentyűinek segítségével megvalósított
 * {@link ControlInterface}.
 */
public class ArrowControlInterface extends ControlInterface implements
		KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		if (mCallback == null) {
			return;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			mCallback.control(new ControlEvent(Type.ACCELERATE_START));
			break;
		case KeyEvent.VK_LEFT:
			mCallback.control(new ControlEvent(Type.TURN_LEFT_START));
			break;
		case KeyEvent.VK_RIGHT:
			mCallback.control(new ControlEvent(Type.TURN_RIGHT_START));
			break;
		case KeyEvent.VK_SPACE:
			mCallback.control(new ControlEvent(Type.FIRE_START));
			break;

		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (mCallback == null) {
			return;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			mCallback.control(new ControlEvent(Type.ACCELERATE_STOP));
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
			mCallback.control(new ControlEvent(Type.TURN_STOP));
			break;
		case KeyEvent.VK_SPACE:
			mCallback.control(new ControlEvent(Type.FIRE_STOP));
			break;

		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
