package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.gui.GameWindow;

import javax.swing.SwingUtilities;

/**
 * A program belépési pontja
 */
public class AsteroidGame {

	public static void main(String[] args) {
		// A grafikus felületet a Swing saját eseménykezelő szálán kell
		// létrehozni
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				showWindow();
			}
		});
	}

	private static void showWindow() {
		GameWindow gameWindow = new GameWindow();
		gameWindow.setSize(300, 300); // FIXME
		gameWindow.setVisible(true);
	}
}