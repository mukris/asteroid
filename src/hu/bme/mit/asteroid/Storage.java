package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.model.ToplistItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * A perzisztens adatok tárolását, kezelését végző osztály
 */
public class Storage {

	private static final int TOPLIST_SIZE = 10;
	private static final String KEY_NAMES = new String("toplist.Names.");
	private static final String KEY_SCORES = new String("toplist.Scores.");
	private static final String KEY_UNLOCKED = new String("level.HighestUnlocked");

	private static Storage sInstance = null;

	private final File mFile = new File("config.xml");
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;
	private Properties mDefaultProperties = new Properties();
	private Properties mProperties;

	private Storage() {
		mDefaultProperties.setProperty(KEY_UNLOCKED, "0");
		for (int i = 0; i < TOPLIST_SIZE; i++) {
			mDefaultProperties.setProperty(KEY_NAMES + i, "-----");
			mDefaultProperties.setProperty(KEY_SCORES + i, "0");
		}
		mProperties = new Properties(mDefaultProperties);
		if (mFile.exists()) {
			try {
				mFileInputStream = new FileInputStream(mFile);
				mProperties.loadFromXML(mFileInputStream);
			} catch (Exception e) {
				System.out.println("Cannot load from file.");
				e.printStackTrace();
			}
		} else {
			try {
				mFile.createNewFile();
				mFileInputStream = new FileInputStream(mFile);
				mFileOutputStream = new FileOutputStream(mFile);
				mProperties.storeToXML(mFileOutputStream, null);
			} catch (Exception e) {
				System.out.println("File creation failed.");
				e.printStackTrace();
			}
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
	public int getHighestUnlockedLevel() {
		return getIntProperty(mProperties, KEY_UNLOCKED);
	}

	/**
	 * Egy pálya feloldott státuszának beállítása
	 * 
	 * @param levelID
	 *            A pálya száma
	 */
	public void setLevelUnlocked(int levelID) {
		if (levelID > getIntProperty(mProperties, KEY_UNLOCKED)) {
			mProperties.setProperty(KEY_UNLOCKED, "" + levelID);
			try {
				mFileOutputStream = new FileOutputStream(mFile);
				mProperties.storeToXML(mFileOutputStream, null);
			} catch (IOException e) {
				System.out.println("Could not save file.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * A toplista elemeinek beolvasása
	 * 
	 * @return A toplista elemei
	 */
	public ArrayList<ToplistItem> getToplistItems() {
		ArrayList<ToplistItem> toplistItems = new ArrayList<>();
		for (int i = 0; i < TOPLIST_SIZE; i++) {
			String name = mProperties.getProperty(KEY_NAMES + i);
			toplistItems.add(new ToplistItem(name, getIntProperty(mProperties, KEY_SCORES + i)));
		}
		return toplistItems;
	}

	/**
	 * Toplisa elemek mentése
	 * 
	 * @param items
	 */
	public void saveToplistItems(ArrayList<ToplistItem> items) {
		for (int i = 0; i < TOPLIST_SIZE; i++) {
			String name = items.get(i).getName();
			mProperties.setProperty(KEY_NAMES + i, name);
			String point = Integer.toString(items.get(i).getPoints());
			mProperties.setProperty(KEY_SCORES + i, point);
		}
		try {
			mFileOutputStream = new FileOutputStream(mFile);
			mProperties.storeToXML(mFileOutputStream, null);
		} catch (IOException e) {
			System.out.println("Could not save file.");
			e.printStackTrace();
		}
	}

	private int getIntProperty(Properties properties, String key) {
		String value = properties.getProperty(key);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mProperties.storeToXML(mFileOutputStream, null);
	}
}
