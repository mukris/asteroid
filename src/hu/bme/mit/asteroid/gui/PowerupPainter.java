package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.model.Powerup;
import hu.bme.mit.asteroid.model.Vector2D;

import java.awt.Graphics;
import java.awt.Image;

public class PowerupPainter extends Painter {

	private final Image mImage;
	
	public PowerupPainter(Image image) {
		mImage = image.getScaledInstance(Powerup.RADIUS * 2, Powerup.RADIUS * 2, Image.SCALE_SMOOTH);
	}

	public void paint(Graphics g, Powerup powerup) {
		final Vector2D position = powerup.getPosition();
		paintInternal(g, mImage, position);
	}
}
