package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.Powerup;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.ToplistItem;
import hu.bme.mit.asteroid.model.Weapon;
import hu.bme.mit.asteroid.player.Player;

import java.awt.Color;
import java.awt.Font;
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
	private static final long serialVersionUID = 9104240694933170699L;

	private static final int FONT_SIZE = 16;
	private static final Font mFont = new Font("Serif", Font.PLAIN, FONT_SIZE);
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
			mImageBackground = ImageIO.read(new File("res/space_1.jpg"));
			Image imageSpaceship1Default = ImageIO.read(new File("res/Spaceship_G_nofire.png"));
			Image imageSpaceship1Acc = ImageIO.read(new File("res/Spaceship_G_fire.png"));
			Image imageSpaceship2Default = ImageIO.read(new File("res/Spaceship_R_nofire.png"));
			Image imageSpaceship2Acc = ImageIO.read(new File("res/Spaceship_R_fire.png"));
			mSpaceshipPainter = new SpaceShipPainter(imageSpaceship1Default, imageSpaceship1Acc,
					imageSpaceship2Default, imageSpaceship2Acc);
			
			Image imageAsteroidSmall = ImageIO.read(new File("res/asteroid_1.png"));
			Image imageAsteroidMedium = ImageIO.read(new File("res/asteroid_2.png"));
			Image imageAsteroidLarge = ImageIO.read(new File("res/asteroid_3.png"));
			mAsteroidPainter = new AsteroidPainter(imageAsteroidSmall,imageAsteroidMedium,imageAsteroidLarge);
			
			Image imageWeapon = ImageIO.read(new File("res/weapon.png"));
			mWeaponPainter = new WeaponPainter(imageWeapon);
			
			Image imagePowerup = ImageIO.read(new File("res/powerup.png"));
			mPowerupPainter = new PowerupPainter(imagePowerup);
			
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
		final AffineTransform originalTransform = g2.getTransform();
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

			List<Asteroid> asteroids = mGameState.getAsteroids();
			synchronized (asteroids) {
				for (Asteroid asteroid : asteroids) {
					mAsteroidPainter.paint(g, asteroid);
				}
			}

			List<Powerup> powerups = mGameState.getPowerups();
			synchronized (powerups) {
				for (Powerup powerup : powerups) {
					mPowerupPainter.paint(g, powerup);
				}
			}

			List<Weapon> weapons1 = spaceShip1.getWeapons();
			synchronized (weapons1) {
				for (Weapon weapon : weapons1) {
					mWeaponPainter.paint(g, weapon);
				}
			}

			if (isMultiplayer) {
				List<Weapon> weapons2 = spaceShip2.getWeapons();
				synchronized (weapons2) {
					for (Weapon weapon : weapons2) {
						mWeaponPainter.paint(g, weapon);
					}
				}

				mSpaceshipPainter.paint(g, spaceShip2, false);
			}
			mSpaceshipPainter.paint(g, spaceShip1, true);

			g2.setTransform(originalTransform);
			g2.setFont(mFont);
			printStats(g2, mGameState.getPlayer1State(), 10, 10);
			if (isMultiplayer) {
				printStats(g2, mGameState.getPlayer2State(), getWidth() - 100, 10);
			}
		}
	}

	/**
	 * Játékos státuszának kiírása
	 * 
	 * @param g
	 *            A "vászon", ahova írunk
	 * @param state
	 *            A játékos {@link Player.State}-je
	 * @param posX
	 *            A kiírás bal koordinátája
	 * @param posY
	 *            A kíírás felső koordninátája
	 */
	private void printStats(Graphics g, Player.State state, int posX, int posY) {
		synchronized (state) {
			g.drawString("Lives: " + state.getLives(), posX, FONT_SIZE + posY);
			g.drawString("Points: " + state.getPoints(), posX, FONT_SIZE * 2 + posY);
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
