package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.model.SpaceShip;

import java.awt.Color;
import java.awt.Graphics;

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

	public GameField(GameWindow gameWindow) {
		super(gameWindow);
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

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// TODO háttér, aszteroidák, fegyverek, powerupok, űrhajók kirajzolása
		if (mGameState != null) {
			SpaceShip spaceShip = mGameState.getSpaceShip1();
			g.setColor(Color.BLACK);
			g.drawOval((int) spaceShip.getPosition().getX(), (int) spaceShip.getPosition().getY(),
					spaceShip.getRadius(), (int) (spaceShip.getRadius() * 1.5f));
		}
	}
	
	@Override
	public boolean isFocusable() {
		return true;
	}
}
