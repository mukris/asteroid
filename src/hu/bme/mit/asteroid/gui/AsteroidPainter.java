package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.model.Asteroid;
import hu.bme.mit.asteroid.model.SpaceShip;
import hu.bme.mit.asteroid.model.Vector2D;

import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

public class AsteroidPainter extends Painter {

	private final Image mSmallImage;
	private final Image mMediumImage;
	private final Image mLargeImage;
	private final int mSmallImageOffsetHorizontal;
	private final int mSmallImageOffsetVertical;
	private final int mMediumImageOffsetHorizontal;
	private final int mMediumImageOffsetVertical;
	private final int mLargeImageOffsetHorizontal;
	private final int mLargeImageOffsetVertical;

	
	public AsteroidPainter(Image imageSmall, Image imageMedium, Image imageLarge) {
		mSmallImage = imageSmall.getScaledInstance(Asteroid.RADIUS_SMALL * 2, Asteroid.RADIUS_SMALL * 2, Image.SCALE_SMOOTH);
		mMediumImage = imageMedium.getScaledInstance(Asteroid.RADIUS_MEDIUM * 2, Asteroid.RADIUS_MEDIUM * 2, Image.SCALE_SMOOTH);
		mLargeImage = imageLarge.getScaledInstance(Asteroid.RADIUS_LARGE * 2, Asteroid.RADIUS_LARGE * 2, Image.SCALE_SMOOTH);
		
		mSmallImageOffsetHorizontal = mSmallImage.getWidth(null) / 2;
		mSmallImageOffsetVertical = mSmallImage.getHeight(null) / 2;
		mMediumImageOffsetHorizontal = mMediumImage.getWidth(null) / 2;
		mMediumImageOffsetVertical = mMediumImage.getHeight(null) / 2;
		mLargeImageOffsetHorizontal = mLargeImage.getWidth(null) / 2;
		mLargeImageOffsetVertical = mLargeImage.getHeight(null) / 2;
	}
	
	public void paint(Graphics g, Asteroid asteroid) {
		Vector2D position = asteroid.getPosition();
		
		Image smallAsteroidImage = mSmallImage;
		Image mediumAsteroidImage = mMediumImage;
		Image largeAsteroidImage = mLargeImage;
					
		switch(asteroid.getType()) {
			case SMALL: 
				g.drawImage(smallAsteroidImage, (int) position.getX() - mSmallImageOffsetHorizontal, (int) position.getY() - mSmallImageOffsetVertical, null);
				break;
			case MEDIUM: 
				g.drawImage(mediumAsteroidImage, (int) position.getX() - mMediumImageOffsetHorizontal, (int) position.getY() - mMediumImageOffsetVertical, null);
				break;
			case LARGE: 
				g.drawImage(largeAsteroidImage, (int) position.getX() - mLargeImageOffsetHorizontal, (int) position.getY() - mLargeImageOffsetVertical, null);
				break;	
		}
				
	}
}
