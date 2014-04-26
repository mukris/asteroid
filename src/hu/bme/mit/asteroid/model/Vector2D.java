package hu.bme.mit.asteroid.model;

import hu.bme.mit.asteroid.AsteroidGame;

import java.io.Serializable;

/**
 * Két dimenziós vektor
 */
public class Vector2D implements Cloneable, Serializable {
	private static final long serialVersionUID = 2869327434602565264L;
	public final static int minDistance = 30;
	
	private float mX;
	private float mY;

	public Vector2D() {
		mX = mY = 0;
	}

	public Vector2D(float x, float y) {
		mX = x;
		mY = y;
	}

	public Vector2D(float length, int direction) {
		mX = (float) (length * Math.cos(direction * Math.PI / 180));
		mY = (float) (length * Math.sin(direction * Math.PI / 180));
	}

	@Override
	public Vector2D clone() {
		return new Vector2D(mX, mY);
	}

	public float getX() {
		return mX;
	}

	public void setX(float x) {
		mX = x;
	}

	public float getY() {
		return mY;
	}

	public void setY(float y) {
		mY = y;
	}

	public Vector2D add(Vector2D vector) {
		mX += vector.mX;
		mY += vector.mY;
		return this;
	}

	public Vector2D multiply(float multiplier) {
		setLength(getLength() * multiplier);
		return this;
	}

	public int getDirection() {
		return (int) (Math.atan2(mY, mX) * 180 / Math.PI);
	}

	public void setDirection(int direction) {
		mX = (float) (getLength() * Math.cos(direction * Math.PI / 180));
		mY = (float) (getLength() * Math.sin(direction * Math.PI / 180));
	}

	public float getLength() {
		return (float) Math.sqrt(mX * mX + mY * mY);
	}

	public void setLength(float length) {
		mX = (float) (length * Math.cos(getDirection() * Math.PI / 180));
		mY = (float) (length * Math.sin(getDirection() * Math.PI / 180));
	}
	
	static float getDistance(Vector2D v1, Vector2D v2) {
		return (float) Math.sqrt((v1.mX - v2.mX) * (v1.mX -v2.mX) + (v1.mY - v2.mY) * (v1.mY - v2.mY));
	}
	
	//véletlen koordinátát generál a megadott térrészben
	//int mert a képpontok intek
	//4 térrészben generál random pontot, a végén ebből a 4 térrészből választ egyet
	//nincs jobb ötletem
	
	public void randomPositionGenerator(int minDistance) {
		int maxX = AsteroidGame.WINDOW_SIZE_X;
		int maxY = AsteroidGame.WINDOW_SIZE_Y;
		int centerX = maxX / 2;
		int centerY = maxY / 2;
		
		int[] tmp_mX = new int[3];
		int[] tmp_mY = new int[3];
		
			//felső rész X koordináta
			tmp_mX[0] = (int)(Math.random() * maxX);
			//felső rész Y koordináta
			tmp_mY[0] = (int)(Math.random() * (centerY - minDistance));
			
			//bal oldal X koordináta
			tmp_mX[1] = (int)(Math.random() * (centerX - minDistance));
			//bal oldal Y koordináta
			tmp_mY[1] = (int)(Math.random() * maxY);
			
			//jobb oldal X koordináta
			tmp_mX[2] = (centerX + minDistance) + (int)(Math.random() * ((maxX - (centerX + minDistance)) + 1));
			//jobb oldal Y koordináta
			tmp_mY[2] = (int)(Math.random() * maxY);
			
			//alsó rész X koordináta
			tmp_mX[3] = (int)(Math.random() * maxX);
			//alsó rész Y koordináta
			tmp_mY[3] = (centerY + minDistance) + (int)(Math.random() * ((maxY - (centerY + minDistance)) + 1));
			
			//0-4 generál egy egész számot, hogy kiválassza a térrészt
			int i = (int)(Math.random() * 4);
			
			mX = (float) tmp_mX[i];
			mY = (float) tmp_mY[i];
	}
	
	
	//véletlen irányt generál. az irány lehet a window bármely pontja
	public void randomDirectionGenerator() {
		mX = (float) (Math.random() * AsteroidGame.WINDOW_SIZE_X);
		mY = (float) (Math.random() * AsteroidGame.WINDOW_SIZE_Y);
	}
	

}
