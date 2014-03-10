package hu.bme.mit.asteroid.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WADControlInterface extends ControlInterface implements
		KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:

			break;
		case KeyEvent.VK_A:

			break;
		case KeyEvent.VK_D:

			break;
		case KeyEvent.VK_CONTROL:

			break;

		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:

			break;
		case KeyEvent.VK_A:

			break;
		case KeyEvent.VK_D:

			break;
		case KeyEvent.VK_CONTROL:

			break;

		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
