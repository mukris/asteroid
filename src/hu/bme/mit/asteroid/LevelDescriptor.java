package hu.bme.mit.asteroid;

/**
 * Egy pálya fő tulajdonságait tároló osztály. A {@link GameState} osztállyal
 * ellentétben nem az aktuális állapotot tárolja, hanem csak a főbb
 * paramétereket.
 */
public class LevelDescriptor {

	private boolean mIsUnlocked;
	private final int mNumAsteroidLarge;
	private final int mNumAsteroidMedium;
	private final int mNumAsteroidSmall;

	/**
	 * Konstruktor
	 * 
	 * A pályát alapértelmezés szerint nem feloldottnak állítja be
	 * 
	 * @param numAsteroidLarge
	 *            Nagy aszteroidák száma
	 * @param numAsteroidMedium
	 *            Közepes aszteroidák száma
	 * @param numAsteroidSmall
	 *            Kis aszteroidák száma
	 */
	public LevelDescriptor(int numAsteroidLarge, int numAsteroidMedium, int numAsteroidSmall) {
		this(numAsteroidLarge, numAsteroidMedium, numAsteroidSmall, false);
	}

	/**
	 * Konstruktor
	 * 
	 * @param numAsteroidLarge
	 *            Nagy aszteroidák száma
	 * @param numAsteroidMedium
	 *            Közepes aszteroidák száma
	 * @param numAsteroidSmall
	 *            Kis aszteroidák száma
	 * @param isUnlocked
	 *            Fel van-e oldva a pálya
	 */
	public LevelDescriptor(int numAsteroidLarge, int numAsteroidMedium, int numAsteroidSmall, boolean isUnlocked) {
		mIsUnlocked = isUnlocked;
		mNumAsteroidLarge = numAsteroidLarge;
		mNumAsteroidMedium = numAsteroidMedium;
		mNumAsteroidSmall = numAsteroidSmall;
	}

	/**
	 * Fel van oldva?
	 * 
	 * @return True ha igen, false ha nem
	 */
	public boolean isUnlocked() {
		return mIsUnlocked;
	}

	/**
	 * Feloldott státusz beállítása
	 * 
	 * @param isUnlocked
	 *            True ha igen, false ha nem
	 */
	public void setUnlocked(boolean isUnlocked) {
		mIsUnlocked = isUnlocked;
	}

	/**
	 * Nagy aszteroidák számának lekérdezése
	 * 
	 * @return Nagy aszteroidák száma
	 */
	public int getNumAsteroidLarge() {
		return mNumAsteroidLarge;
	}

	/**
	 * Közepes aszteroidák számának lekérdezése
	 * 
	 * @return Közepes aszteroidák száma
	 */
	public int getNumAsteroidMedium() {
		return mNumAsteroidMedium;
	}

	/**
	 * Kis aszteroidák számának lekérdezése
	 * 
	 * @return Kis aszteroidák száma
	 */
	public int getNumAsteroidSmall() {
		return mNumAsteroidSmall;
	}
}
