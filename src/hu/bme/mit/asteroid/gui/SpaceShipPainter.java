package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.Vector2D;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A {@link SpaceShip} objektum állapotának megfelelő kirajzolását végző osztály
 */
public class SpaceShipPainter extends Painter {

	private final Image mDefaultImage1;
	private final Image mDefaultImage2;
	private final Image mAcceleratingImage1;
	private final Image mAcceleratingImage2;
	private final int mImageOffsetHorizontal;
	private final int mImageOffsetVertical;

	/**
	 * Létrehoz egy új {@link SpaceShipPainter} objektumot, ami egy
	 * {@link SpaceShip} képét annak állapotától függően kirajzolja a
	 * játékmezőre
	 * 
	 * @param defaultImage
	 *            Az űrhajó normál képe
	 * @param acceleratingImage
	 *            Az űrhajó képe bekapcsolt rakétával
	 */
	public SpaceShipPainter(Image defaultImage1, Image acceleratingImage1, Image defaultImage2, Image acceleratingImage2) {
		mDefaultImage1 = defaultImage1
				.getScaledInstance(SpaceShip.RADIUS * 2, SpaceShip.RADIUS * 2, Image.SCALE_SMOOTH);
		mAcceleratingImage1 = acceleratingImage1.getScaledInstance(SpaceShip.RADIUS * 2, SpaceShip.RADIUS * 2,
				Image.SCALE_SMOOTH);
		mDefaultImage2 = defaultImage2
				.getScaledInstance(SpaceShip.RADIUS * 2, SpaceShip.RADIUS * 2, Image.SCALE_SMOOTH);
		mAcceleratingImage2 = acceleratingImage2.getScaledInstance(SpaceShip.RADIUS * 2, SpaceShip.RADIUS * 2,
				Image.SCALE_SMOOTH);
		mImageOffsetHorizontal = mDefaultImage1.getWidth(null) / 2;
		mImageOffsetVertical = mDefaultImage1.getHeight(null) / 2;
	}

	/**
	 * A tényleges rajzolást végző függvény
	 * 
	 * @param g
	 *            A "vászon", ahova rajzolunk
	 * @param spaceShip
	 *            A kirajzolás alapjául szolgáló {@link SpaceShip}
	 */
	public void paint(Graphics g, SpaceShip spaceShip, boolean isPlayerOne) {
		// Ha nem sebezhető, akkor villog
		if (!spaceShip.isVulnerable()) {
			if (spaceShip.getUnvulnerableFor() % 200 > 100) {
				return;
			}
		}

		Vector2D position = spaceShip.getPosition();

		// Megfelelő kép kiválasztása az alapján, hogy gyorsul-e az űrhajó
		Image spaceshipImage;
		if (isPlayerOne) {
			spaceshipImage = (spaceShip.isAccelerating()) ? mAcceleratingImage1 : mDefaultImage1;
		} else {
			spaceshipImage = (spaceShip.isAccelerating()) ? mAcceleratingImage2 : mDefaultImage2;
		}

		// Elforgatott kép kiszámolása
		// Üres "vászon" létrehozása
		BufferedImage bufferedImage = new BufferedImage(mImageOffsetHorizontal * 2, mImageOffsetVertical * 2,
				Transparency.TRANSLUCENT);
		Graphics2D g2 = bufferedImage.createGraphics();
		AffineTransform transform = new AffineTransform();

		// Elforgatás a kép középpontja körül transzformáció
		transform.rotate(spaceShip.getDirection(), mImageOffsetHorizontal, mImageOffsetVertical);
		// Eredeti kép kirajzolása megfelelően elforgatva a vászonra
		g2.drawImage(spaceshipImage, transform, null);

		// Az elforgatott kép kirajzolása megfelelő pozícióban az esetleges
		// átlapolódásokkal együtt
		paintInternal(g, bufferedImage, position);

		// Az elforgatáshoz használt vászon memóriaterületének felszabadítása
		g2.dispose();
	}
}
