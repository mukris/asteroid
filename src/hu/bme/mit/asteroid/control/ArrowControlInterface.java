package hu.bme.mit.asteroid.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A billentyűzet nyíl, illetve szóköz billentyűinek segítségével megvalósított
 * {@link ControlInterface}.
 */
public class ArrowControlInterface extends ControlInterface implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:

			break;
		case KeyEvent.VK_LEFT:

			break;
		case KeyEvent.VK_RIGHT:

			break;
		case KeyEvent.VK_SPACE:

			break;

		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:

			break;
		case KeyEvent.VK_LEFT:

			break;
		case KeyEvent.VK_RIGHT:

			break;
		case KeyEvent.VK_SPACE:

			break;

		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
