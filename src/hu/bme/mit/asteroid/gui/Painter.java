package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.model.Vector2D;

import java.awt.Graphics;
import java.awt.Image;

/**
 * A játékmező elemeit kirajzoló objektumok közös absztrakt ősosztálya
 */
public abstract class Painter {

	protected int mWidth = 0;
	protected int mHeight = 0;

	public void setWidth(int width) {
		mWidth = width;
	}

	public void setHeight(int height) {
		mHeight = height;
	}

	public void setDimensions(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	/**
	 * A tényleges kirajzolást végző függvény. Figyelembe veszi, hogy a pálya
	 * végtelenített, ezért ha egy objektum a pálya szélére kerül, a túloldalt
	 * visszajön.
	 * 
	 * @param g
	 *            A vászon, amire rajzolunk
	 * @param image
	 *            A kirajzolandó kép
	 * @param centerPosition
	 *            A kirajzolandó kép középpontja
	 */
	protected void paintInternal(Graphics g, Image image, Vector2D centerPosition) {
		final int imageOffsetHorizontal = image.getWidth(null) / 2;
		final int imageOffsetVertical = image.getHeight(null) / 2;

		// kirajzolás az eredeti pozícióba
		g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal),
				(int) (centerPosition.getY() - imageOffsetVertical), null);

		// ha bal oldalt kilóg
		if (centerPosition.getX() - imageOffsetHorizontal < 0) {
			// kirajzolás a jobb oldalra
			g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal + mWidth),
					(int) (centerPosition.getY() - imageOffsetVertical), null);

			// ha felül kilóg
			if (centerPosition.getY() - imageOffsetVertical < 0) {
				// kirajzolás felül
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal), (int) (centerPosition.getY()
						- imageOffsetVertical + mHeight), null);
				// kirajzolás jobb oldalt felül
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal + mWidth),
						(int) (centerPosition.getY() - imageOffsetVertical + mHeight), null);
			}
			// ha alul kilóg
			else if (centerPosition.getY() + imageOffsetVertical > mHeight) {
				// kirajzolás alul
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal), (int) (centerPosition.getY()
						- imageOffsetVertical - mHeight), null);
				// kirajzolás jobb oldalt alul
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal + mWidth),
						(int) (centerPosition.getY() - imageOffsetVertical - mHeight), null);
			}
		}
		// ha jobb oldalt kilóg
		else if (centerPosition.getX() + imageOffsetHorizontal > mWidth) {
			// kirajzolás bal oldalt
			g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal - mWidth),
					(int) (centerPosition.getY() - imageOffsetVertical), null);

			// ha felül kilóg
			if (centerPosition.getY() - imageOffsetVertical < 0) {
				// kirajzolás alul
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal), (int) (centerPosition.getY()
						- imageOffsetVertical + mHeight), null);
				// kirajzolás bal oldalt alul
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal - mWidth),
						(int) (centerPosition.getY() - imageOffsetVertical + mHeight), null);
			}
			// ha alul kilóg
			else if (centerPosition.getY() + imageOffsetVertical > mHeight) {
				// kirajzolás felül
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal), (int) (centerPosition.getY()
						- imageOffsetVertical - mHeight), null);
				// kirajzolás bal oldalt felül
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal - mWidth),
						(int) (centerPosition.getY() - imageOffsetVertical - mHeight), null);
			}
		}
		// ha oldalt nem lóg ki
		else {
			// ha felül kilóg
			if (centerPosition.getY() - imageOffsetVertical < 0) {
				// kirajzolás alul
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal), (int) (centerPosition.getY()
						- imageOffsetVertical + mHeight), null);
			}
			// ha alul kilóg
			else if (centerPosition.getY() + imageOffsetVertical > mHeight) {
				// kirajzolás felül
				g.drawImage(image, (int) (centerPosition.getX() - imageOffsetHorizontal), (int) (centerPosition.getY()
						- imageOffsetVertical - mHeight), null);
			}
		}
	}
}
