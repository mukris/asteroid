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

	private final Image mDefaultImage;
	private final Image mAcceleratingImage;
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
	public SpaceShipPainter(Image defaultImage, Image acceleratingImage) {
		mDefaultImage = defaultImage.getScaledInstance(SpaceShip.RADIUS * 2, SpaceShip.RADIUS * 2,
				Image.SCALE_SMOOTH);
		mAcceleratingImage = acceleratingImage.getScaledInstance(SpaceShip.RADIUS * 2,
				SpaceShip.RADIUS * 2, Image.SCALE_SMOOTH);
		mImageOffsetHorizontal = mDefaultImage.getWidth(null) / 2;
		mImageOffsetVertical = mDefaultImage.getHeight(null) / 2;
	}

	/**
	 * A tényleges rajzolást végző függvény
	 * 
	 * @param g
	 *            A "vászon", ahova rajzolunk
	 * @param spaceShip
	 *            A kirajzolás alapjául szolgáló {@link SpaceShip}
	 */
	public void paint(Graphics g, SpaceShip spaceShip) {
		// Ha nem sebezhető, akkor villog
		if (!spaceShip.isVulnerable()) {
			if (spaceShip.getUnvulnerableFor() % 200 > 100) {
				return;
			}
		}

		Vector2D position = spaceShip.getPosition();

		// Megfelelő kép kiválasztása az alapján, hogy gyorsul-e az űrhajó
		Image spaceshipImage = (spaceShip.isAccelerating()) ? mAcceleratingImage : mDefaultImage;

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
