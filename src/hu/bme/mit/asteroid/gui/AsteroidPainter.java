package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.Vector2D;

import java.awt.Graphics;
import java.awt.Image;

public class AsteroidPainter extends Painter {

	private final Image mSmallImage;
	private final Image mMediumImage;
	private final Image mLargeImage;

	public AsteroidPainter(Image imageSmall, Image imageMedium, Image imageLarge) {
		mSmallImage = imageSmall.getScaledInstance(Asteroid.Type.SMALL.getRadius() * 2,
				Asteroid.Type.SMALL.getRadius() * 2, Image.SCALE_SMOOTH);
		mMediumImage = imageMedium.getScaledInstance(Asteroid.Type.MEDIUM.getRadius() * 2,
				Asteroid.Type.MEDIUM.getRadius() * 2, Image.SCALE_SMOOTH);
		mLargeImage = imageLarge.getScaledInstance(Asteroid.Type.LARGE.getRadius() * 2,
				Asteroid.Type.LARGE.getRadius() * 2, Image.SCALE_SMOOTH);
	}

	public void paint(Graphics g, Asteroid asteroid) {
		final Vector2D position = asteroid.getPosition();

		switch (asteroid.getType()) {
		case SMALL:
			paintInternal(g, mSmallImage, position);
			break;
		case MEDIUM:
			paintInternal(g, mMediumImage, position);
			break;
		case LARGE:
			paintInternal(g, mLargeImage, position);
			break;
		}

	}
}
