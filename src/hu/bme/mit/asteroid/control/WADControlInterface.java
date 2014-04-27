package hu.bme.mit.asteroid.control;

import hu.bme.mit.asteroid.control.ControlEvent.Type;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A billentyűzet "W", "A" "D", illetve control billentyűinek segítségével
 * megvalósított {@link ControlInterface}.
 */
public class WADControlInterface extends ControlInterface implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO - is OK?
		if (mCallback == null) {
			return;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			mCallback.control(new ControlEvent(Type.ACCELERATE_START));
			break;
		case KeyEvent.VK_A:
			mCallback.control(new ControlEvent(Type.TURN_LEFT_START));
			break;
		case KeyEvent.VK_D:
			mCallback.control(new ControlEvent(Type.TURN_RIGHT_START));
			break;
		case KeyEvent.VK_CONTROL:
			mCallback.control(new ControlEvent(Type.FIRE_START));
			break;

		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO - is OK?
		if (mCallback == null) {
			return;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			mCallback.control(new ControlEvent(Type.ACCELERATE_STOP));
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_D:
			mCallback.control(new ControlEvent(Type.TURN_STOP));
			break;
		case KeyEvent.VK_CONTROL:
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
