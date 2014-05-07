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
		mSmallImage = imageSmall.getScaledInstance(Asteroid.RADIUS_SMALL * 2, Asteroid.RADIUS_SMALL * 2,
				Image.SCALE_SMOOTH);
		mMediumImage = imageMedium.getScaledInstance(Asteroid.RADIUS_MEDIUM * 2, Asteroid.RADIUS_MEDIUM * 2,
				Image.SCALE_SMOOTH);
		mLargeImage = imageLarge.getScaledInstance(Asteroid.RADIUS_LARGE * 2, Asteroid.RADIUS_LARGE * 2,
				Image.SCALE_SMOOTH);
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
