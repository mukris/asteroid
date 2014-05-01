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
	private static Storage sInstance = null;
	private static final File sFile = new File("config.xml");
	private static FileInputStream sFileInputStream;
	private static FileOutputStream sFileOutputStream;
	private static Properties sDefaultProperties;
	private static Properties mProperties;
	private static final String mNkey = new String("toplist.Names.");
	private static final String mSkey = new String("toplist.Score.");
	private static final String mHkey = new String("level.HighestUnlocked");

	private Storage() {
		sDefaultProperties = new Properties();
		sDefaultProperties.setProperty(mHkey, "0");
		for (int i = 0; i < TOPLIST_SIZE; i++) {
			sDefaultProperties.setProperty(mNkey + i, "-empty-");
			sDefaultProperties.setProperty(mSkey + i, "0");
		}
		mProperties = new Properties(sDefaultProperties);
		if (sFile.exists()) {
			try {
				sFileInputStream = new FileInputStream(sFile);
				// sFileOutputStream = new FileOutputStream(sFile);
				mProperties.loadFromXML(sFileInputStream);
			} catch (Exception e) {
				System.out.println("Cannot load from file.");
				e.printStackTrace();
			}
		} else {
			try {
				sFile.createNewFile();
				sFileInputStream = new FileInputStream(sFile);
				sFileOutputStream = new FileOutputStream(sFile);
				mProperties.storeToXML(sFileOutputStream, null);
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
		return getIntProperty(mProperties, mHkey);
	}

	/**
	 * Egy pálya feloldott státuszának beállítása
	 * 
	 * @param levelID
	 *            A pálya száma
	 * @param unlocked
	 *            True ha igen, false ha nem
	 */
	public void setLevelUnlocked(int levelID) {
		if (levelID > getIntProperty(mProperties, mHkey)) {
			mProperties.setProperty(mHkey, "" + levelID);
			try {
				sFileOutputStream = new FileOutputStream(sFile);
				mProperties.storeToXML(sFileOutputStream, null);
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
			String name = mProperties.getProperty(mNkey + i);
			toplistItems.add(new ToplistItem(name, getIntProperty(mProperties, mSkey + i)));
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
			mProperties.setProperty(mNkey + i, name);
			String point = Integer.toString(items.get(i).getPoints());
			mProperties.setProperty(mSkey + i, point);
		}
		try {
			sFileOutputStream = new FileOutputStream(sFile);
			mProperties.storeToXML(sFileOutputStream, null);
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
		mProperties.storeToXML(sFileOutputStream, null);
	}
}
