package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.model.ToplistItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * A perzisztens adatok tárolását, kezelését végző osztály
 */
public class Storage {

	private static final int TOPLIST_SIZE = 10;
	private static Storage sInstance = null;
	private static File sFile = new File("config.xml");
	private static FileInputStream sFileInputStream;
	private static Properties sDefaultProperties;

	private Storage() {
		try {
			sFileInputStream = new FileInputStream(sFile);
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't open file.");
		}
		sDefaultProperties = new Properties();
		sDefaultProperties.setProperty("level.HighestUnlocked", "0");
		for (int i = 0; i < TOPLIST_SIZE; i++) {
			String key = "toplist.Names." + i;
			sDefaultProperties.setProperty(key, "-empty-");
			key = "toplist.Score." + i;
			sDefaultProperties.setProperty(key, "0");
		}
	}

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
	 * A legmagasabb sorszámú feloldott pálya számának beolvasása
	 * 
	 * @return A legmagasabb sorszámú feloldott pálya száma
	 */
	public static int getHighestUnlockedLevel() {
		Properties properties = new Properties(sDefaultProperties);
		try {
			properties.loadFromXML(sFileInputStream);
		} catch (Exception e) {
			System.out.println("Cannot load from file.");
			e.printStackTrace();
		}
		String highestUnlocked = properties.getProperty("level.HighestUnlocked");
		return Integer.getInteger(highestUnlocked, 0);
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
		Properties properties = new Properties(sDefaultProperties);
		try {
			properties.loadFromXML(sFileInputStream);
		} catch (Exception e) {
			System.out.println("Cannot load from file.");
			e.printStackTrace();
		}
		for (int i = 0; i < TOPLIST_SIZE; i++) {
			String key = "toplist.Names." + i;
			String name = properties.getProperty(key);
			key = "toplist.Score." + i;
			String point = properties.getProperty(key);
			toplistItems.add(new ToplistItem(name, Integer.getInteger(point)));
		}
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
