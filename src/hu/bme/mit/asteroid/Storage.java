package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.model.ToplistItem;

import java.util.ArrayList;

/**
 * A perzisztens adatok tárolását, kezelését végző osztály
 */
public class Storage {
	
	private static Storage sInstance = null;
	
	public static Storage getInstance() {
		if(sInstance == null) {
			sInstance = new Storage();
		}
		return sInstance;
	}

	public static ArrayList<Boolean> getLevelUnlocks() {
		ArrayList<Boolean> unlocks = new ArrayList<>();
		// TODO beolvasás fájlból
		return unlocks;
	}

	public static void setLevelUnlocked(int levelID, boolean unlocked) {
		// TODO kiírás fájlba
	}

	public static ArrayList<ToplistItem> getToplistItems() {
		ArrayList<ToplistItem> toplistItems = new ArrayList<>();
		// TODO beolvasás fájlból
		return toplistItems;
	}

	public static void saveToplistItems(ArrayList<ToplistItem> items) {
		// TODO kiírás fájlba
	}
}
