package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.Weapon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * A játék grafikus megjelenítését végző osztály
 */
public class GameField extends GamePanel {

	private static final long serialVersionUID = 6958968330216408636L;

	private GameState mGameState;
	private SpaceShipPainter mSpaceshipPainter;
	private AsteroidPainter mAsteroidPainter;
	private WeaponPainter mWeaponPainter;
	private PowerupPainter mPowerupPainter;
	private Image mImageBackground = null;

	public GameField(GameWindow gameWindow) {
		super(gameWindow);
		try {
			mImageBackground = ImageIO.read(new File("res/space.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO Painterek létrehozása, inicializálása
	}

	/**
	 * A megjelenítés alapjául szolgáló adatok frissítése
	 * 
	 * @param gameState
	 *            Az új állapotokat tároló {@link GameState}.
	 */
	public void update(GameState gameState) {
		if (mGameState == null) {
			mGameState = gameState;
		} else {
			synchronized (mGameState) {
				mGameState.update(gameState);
			}
		}
		repaint();
	}

	/**
	 * Akkor hívódik, ha hálózati hiba történt
	 */
	public void onNetworkError() {
		JOptionPane.showMessageDialog(this, "A hálózati kapcsolat megszakadt", "Hálózati hiba",
				JOptionPane.ERROR_MESSAGE);
		mGameWindow.showPanel(PanelId.GAME_MODE_SELECTOR);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// TODO háttér, aszteroidák, fegyverek, powerupok, űrhajók kirajzolása
		g.drawImage(mImageBackground, 0, 0, null);
		if (mGameState != null) {
			SpaceShip spaceShip = mGameState.getSpaceShip1();
			g.setColor(Color.WHITE);
			g.drawOval((int) spaceShip.getPosition().getX(), (int) spaceShip.getPosition().getY(),
					spaceShip.getRadius(), (int) (spaceShip.getRadius() * 1.5f));

			for (Weapon weapon : spaceShip.getWeapons()) {
				g.drawOval((int) weapon.getPosition().getX(), (int) weapon.getPosition().getY(),
						(int) weapon.getRadius(), (int) weapon.getRadius());
			}
		}
	}

	@Override
	public boolean isFocusable() {
		return true;
	}
}
