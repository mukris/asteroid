package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.model.SpaceShip;

import java.awt.Graphics;
import java.awt.Image;

/**
 * A {@link SpaceShip} objektum állapotának megfelelő kirajzolását végző osztály
 */
public class SpaceShipPainter {

	private Image mDefaultImage;
	private Image mAcceleratingImage;

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
		mDefaultImage = defaultImage;
		mAcceleratingImage = acceleratingImage;
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
		// TODO űrhajó kirajzolása az adatai alapján (pozíció, szög, gyorsul-e)
	}
}
