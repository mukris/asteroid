package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.model.ToplistItem;

import java.util.ArrayList;

/**
 * A perzisztens adatok tárolását, kezelését végző osztály
 */
public class Storage {

	private static Storage sInstance = null;

	/**
	 * Az egyetlen {@link Storage} példány elkérése
	 * 
	 * @return Az egyetlen {@link Storage} példány
	 */
	public static Storage getInstance() {
		if (sInstance == null) {
			sInstance = new Storage();
		}
		return sInstance;
	}

	/**
	 * A pályák feloldottsági állapotának beolvasása
	 * 
	 * @return
	 */
	public static ArrayList<Boolean> getLevelUnlocks() {
		ArrayList<Boolean> unlocks = new ArrayList<>();
		// TODO beolvasás fájlból
		return unlocks;
	}

	/**
	 * Egy pálya feloldott státuszának beállítása
	 * 
	 * @param levelID
	 *            A pálya száma
	 * @param unlocked
	 *            True ha igen, false ha nem
	 */
	public static void setLevelUnlocked(int levelID, boolean unlocked) {
		// TODO kiírás fájlba
	}

	/**
	 * A toplista elemeinek beolvasása
	 * 
	 * @return A toplista elemei
	 */
	public static ArrayList<ToplistItem> getToplistItems() {
		ArrayList<ToplistItem> toplistItems = new ArrayList<>();
		// TODO beolvasás fájlból
		return toplistItems;
	}

	/**
	 * Toplisa elemek mentése
	 * 
	 * @param items
	 */
	public static void saveToplistItems(ArrayList<ToplistItem> items) {
		// TODO kiírás fájlba
	}
}
