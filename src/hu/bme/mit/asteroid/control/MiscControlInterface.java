package hu.bme.mit.asteroid.control;

import hu.bme.mit.asteroid.control.ControlEvent.Type;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A billentyűzet egyéb funkciós billentyűinek segítségével megvalósított
 * {@link ControlInterface}.
 */
public class MiscControlInterface extends ControlInterface implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (mCallback == null) {
			return;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_P:
			mCallback.control(new ControlEvent(Type.INVERT_PAUSE));

		default:
			break;
		}
	}
}
