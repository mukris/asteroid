package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.model.Powerup;
import hu.bme.mit.asteroid.model.Vector2D;
import hu.bme.mit.asteroid.model.Weapon;

import java.awt.Graphics;
import java.awt.Image;

public class PowerupPainter extends Painter {

	private final Image mImage;
	private final int mImageOffsetHorizontal;
	private final int mImageOffsetVertical;
	
	public PowerupPainter(Image image) {
		mImage = image.getScaledInstance(Powerup.RADIUS * 2, Powerup.RADIUS * 2, Image.SCALE_SMOOTH);
		
		mImageOffsetHorizontal = mImage.getWidth(null) / 2;
		mImageOffsetVertical = mImage.getHeight(null) / 2;
	}

	public void paint(Graphics g, Powerup powerup) {
		Vector2D position = powerup.getPosition();
		
		Image powerupImage = mImage;
		
		g.drawImage(powerupImage, (int) position.getX() + mImageOffsetHorizontal, (int) position.getY() + mImageOffsetVertical, null);
		
	}
	
}
