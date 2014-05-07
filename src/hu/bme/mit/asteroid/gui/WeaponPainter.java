package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.model.Vector2D;
import hu.bme.mit.asteroid.model.Weapon;

import java.awt.Graphics;
import java.awt.Image;

public class WeaponPainter extends Painter {

	private final Image mImage;

	public WeaponPainter(Image image) {
		mImage = image.getScaledInstance(Weapon.RADIUS * 2, Weapon.RADIUS * 2, Image.SCALE_SMOOTH);
	}

	public void paint(Graphics g, Weapon weapon) {
		final Vector2D position = weapon.getPosition();
		paintInternal(g, mImage, position);
	}
}
