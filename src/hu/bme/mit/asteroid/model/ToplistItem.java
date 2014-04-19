package hu.bme.mit.asteroid.model;

/**
 * A toplista egyik elemét jelképező osztály
 */
public class ToplistItem {
	private final String mName;
	private final int mPoints;

	public ToplistItem(String name, int point) {
		mName = name;
		mPoints = point;
	}

	public String getName() {
		return mName;
	}

	public int getPoints() {
		return mPoints;
	}
}
