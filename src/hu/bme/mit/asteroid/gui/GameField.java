package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.ToplistItem;
import hu.bme.mit.asteroid.model.Weapon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
	private AffineTransform mReverseYAxisTransform;

	public GameField(GameWindow gameWindow) {
		super(gameWindow);
		try {
			mImageBackground = ImageIO.read(new File("res/space.jpg"));
			Image imageSpaceship = ImageIO.read(new File("res/spaceship.png"));
			mSpaceshipPainter = new SpaceShipPainter(imageSpaceship, imageSpaceship);
		} catch (IOException e) {
			e.printStackTrace();
		}

		mReverseYAxisTransform = new AffineTransform();

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
		Graphics2D g2 = (Graphics2D) g;
		g2.setTransform(mReverseYAxisTransform);

		// TODO aszteroidák, fegyverek, powerupok kirajzolása
		g.drawImage(mImageBackground, 0, 0, null);
		if (mGameState != null) {
			final boolean isMultiplayer = mGameState.isMultiplayer();
			SpaceShip spaceShip1 = mGameState.getSpaceShip1();
			SpaceShip spaceShip2 = null;
			if (isMultiplayer) {
				spaceShip2 = mGameState.getSpaceShip2();
			}
			g.setColor(Color.WHITE);

			List<Weapon> weapons1 = spaceShip1.getWeapons();
			synchronized (weapons1) {
				for (Weapon weapon : weapons1) {
					g.drawOval((int) weapon.getPosition().getX(), (int) weapon.getPosition().getY(),
							(int) weapon.getRadius(), (int) weapon.getRadius());
				}
			}

			if (isMultiplayer) {
				List<Weapon> weapons2 = spaceShip2.getWeapons();
				synchronized (weapons2) {
					for (Weapon weapon : weapons2) {
						g.drawOval((int) weapon.getPosition().getX(), (int) weapon.getPosition().getY(),
								(int) weapon.getRadius(), (int) weapon.getRadius());
					}
				}

				mSpaceshipPainter.paint(g, spaceShip2);
			}
			mSpaceshipPainter.paint(g, spaceShip1);
		}
	}

	@Override
	public boolean isFocusable() {
		return true;
	}

	public void onGameOver() {
		if (mGameState != null) {
			if (!mGameState.isMultiplayer()) {
				int points = mGameState.getPlayer1State().getPoints();
				if (GameManager.getInstance().isEnoughForToplist(points)) {
					String name = "";
					do {
						name = JOptionPane.showInputDialog(this, "Hogy hívnak?", "Felkerültél a toplistára",
								JOptionPane.QUESTION_MESSAGE);
					} while (name.isEmpty());

					ToplistItem toplistItem = new ToplistItem(name, points);
					GameManager.getInstance().addToplistItem(toplistItem);
				}
				mGameWindow.showPanel(PanelId.TOPLIST);
			} else {
				mGameWindow.showPanel(PanelId.GAME_MODE_SELECTOR);
			}
		}
	}

	@Override
	protected void onShow() {
		super.onShow();

		mReverseYAxisTransform = new AffineTransform();
		mReverseYAxisTransform.translate(0, getHeight() - 1);
		mReverseYAxisTransform.scale(1, -1);

		final int width = getWidth();
		final int height = getHeight();
		GameManager.getInstance().updateGameFieldDimensions(width, height);
		mSpaceshipPainter.setDimensions(width, height);
		// TODO painterek beállítása a játékmező méretének megfelelően
	}
}
