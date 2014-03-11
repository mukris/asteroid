package hu.bme.mit.asteroid;

public class LevelDescriptor {
	private boolean mIsUnlocked;
	private final int mNumAsteroidLarge;
	private final int mNumAsteroidMedium;
	private final int mNumAsteroidSmall;

	public LevelDescriptor(int numAsteroidLarge, int numAsteroidMedium,
			int numAsteroidSmall) {
		this(numAsteroidLarge, numAsteroidMedium, numAsteroidSmall, false);
	}

	public LevelDescriptor(int numAsteroidLarge, int numAsteroidMedium,
			int numAsteroidSmall, boolean isUnlocked) {
		mIsUnlocked = isUnlocked;
		mNumAsteroidLarge = numAsteroidLarge;
		mNumAsteroidMedium = numAsteroidMedium;
		mNumAsteroidSmall = numAsteroidSmall;
	}

	public boolean isUnlocked() {
		return mIsUnlocked;
	}

	public void setUnlocked(boolean isUnlocked) {
		mIsUnlocked = isUnlocked;
	}

	public int getNumAsteroidLarge() {
		return mNumAsteroidLarge;
	}

	public int getNumAsteroidMedium() {
		return mNumAsteroidMedium;
	}

	public int getNumAsteroidSmall() {
		return mNumAsteroidSmall;
	}
}
