package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameState;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A játék grafikus megjelenítését végző osztály
 */
public class GameField extends JPanel {

	private static final long serialVersionUID = 6958968330216408636L;

	private GameState mGameState;
	private SpaceShipPainter mSpaceshipPainter;
	private AsteroidPainter mAsteroidPainter;
	private WeaponPainter mWeaponPainter;
	private PowerupPainter mPowerupPainter;

	public GameField() {
		// TODO Painterek létrehozása, inicializálása
	}

	/**
	 * A megjelenítés alapjául szolgáló adatok frissítése
	 * 
	 * @param gameState
	 *            Az új állapotokat tároló {@link GameState}.
	 */
	public void update(GameState gameState) {
		synchronized (mGameState) {
			mGameState.update(gameState);
		}
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// TODO háttér, aszteroidák, fegyverek, powerupok, űrhajók kirajzolása
	}
}
