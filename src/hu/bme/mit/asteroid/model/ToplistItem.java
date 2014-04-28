package hu.bme.mit.asteroid.model;

/**
 * A toplista egyik elemét jelképező osztály
 */
public class ToplistItem implements Comparable<ToplistItem> {
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

	@Override
	public int compareTo(ToplistItem other) {
		if (other == null) {
			throw new NullPointerException();
		}
		return mPoints - other.mPoints;
	}
}
