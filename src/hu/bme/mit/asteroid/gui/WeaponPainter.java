package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.Vector2D;
import hu.bme.mit.asteroid.model.Weapon;

import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

public class WeaponPainter extends Painter {
		
	private final Image mImage;
	private final int mImageOffsetHorizontal;
	private final int mImageOffsetVertical;
		
	public WeaponPainter(Image image) {
		mImage = image.getScaledInstance(Weapon.RADIUS * 2, Weapon.RADIUS * 2, Image.SCALE_SMOOTH);
			
		mImageOffsetHorizontal = mImage.getWidth(null) / 2;
		mImageOffsetVertical = mImage.getHeight(null) / 2;
	}
	
	public void paint(Graphics g, Weapon weapon) {
		Vector2D position = weapon.getPosition();
		
		Image weaponImage = mImage;

		g.drawImage(weaponImage, (int) position.getX() - mImageOffsetHorizontal, (int) position.getY() - mImageOffsetVertical, null);
	
	}
}
