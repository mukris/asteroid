package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.gui.GameWindow;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * A program belépési pontja
 */

public class AsteroidGame {

	public final static int WINDOW_SIZE_X = 800;
	public final static int WINDOW_SIZE_Y = 600;

	public static void main(String[] args) {
		// A grafikus felületet a Swing saját eseménykezelő szálán kell
		// létrehozni
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
				}
				showWindow();
			}
		});
	}

	private static void showWindow() {
		GameWindow gameWindow = new GameWindow();
		gameWindow.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);

		try {
			Image icon = ImageIO.read(new File("res/Spaceship_G_fire.png"));
			gameWindow.setIconImage(icon);
		} catch (Exception e) {
		}

		gameWindow.setVisible(true);
	}
}
