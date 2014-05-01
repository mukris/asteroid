package hu.bme.mit.asteroid.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A hálózati szerver felderítésért felelős osztály
 */
public class NetworkDiscover {

	private static final String MAGIC_STRING = "### Asteroid game magic string ###";
	private static final int PORT = 8765;
	private static final int TIMEOUT = 2000;

	/**
	 * A hálózati felderítés eseményeinek lekezelését lehetővé tevő interfész
	 */
	public interface NetworkDiscoverListener {

		/**
		 * Akkor hívódik, ha új szerver jelent meg a hálózaton.
		 * 
		 * @param address
		 *            A megtalált szerver címe
		 * @param networkDiscover
		 *            Az eseményt kiváltó {@link NetworkDiscover} objektum.
		 *            Szükség esetén lekérdezhető az összes megtalált szerver
		 *            címe, vagy leállítható a további figyelés.
		 */
		public void onDiscover(InetAddress address, NetworkDiscover networkDiscover);

		/**
		 * Akkor hívódik, ha egy szerver már nem elérhető a hálózaton
		 * 
		 * @param address
		 *            A lekapcsolódott szerver címe
		 */
		public void onDiscoveredTimeout(InetAddress address);
	}

	private ArrayList<NetworkDiscoverListener> mListeners;
	private BroadcastSenderThread mSenderThread;
	private BroadcastReceiverThread mReceiverThread;
	private DiscoverWatchdogThread mDiscoverWatchdogThread;
	private Map<InetAddress, Long> mAddresses;

	public NetworkDiscover() {
		mAddresses = new HashMap<>();
		mListeners = new ArrayList<>();
	}

	public NetworkDiscover(NetworkDiscoverListener listener) {
		this();
		addNetworkDiscoverListener(listener);
	}

	public void addNetworkDiscoverListener(NetworkDiscoverListener listener) {
		if (listener != null) {
			synchronized (mListeners) {
				if (!mListeners.contains(listener)) {
					mListeners.add(listener);
				}
			}
		}
	}

	public void removeNetworkDiscoverListener(NetworkDiscoverListener listener) {
		if (listener != null) {
			synchronized (mListeners) {
				mListeners.remove(listener);
			}
		}
	}

	/**
	 * Broadcast üzenetek periodikus küldésének indítása
	 * 
	 * @throws SocketException
	 */
	public void startBroadcasting() throws SocketException {
		if (mSenderThread == null || !mSenderThread.isAlive()) {
			mSenderThread = new BroadcastSenderThread(MAGIC_STRING, PORT);
			mSenderThread.start();
		}
	}

	/**
	 * Broadcast üzenetek küldésének leállítása
	 */
	public void stopBroadcasting() {
		if (mSenderThread != null && mSenderThread.isAlive()) {
			mSenderThread.interrupt();
			try {
				mSenderThread.join();
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Broadcast üzenetek figyelésének indítása
	 */
	public void startListening() {
		clearDiscoveredAddresses();
		if (mReceiverThread == null || !mReceiverThread.isAlive()) {
			mReceiverThread = new BroadcastReceiverThread(MAGIC_STRING, PORT);
			mReceiverThread.start();
		}
		if (mDiscoverWatchdogThread == null || !mDiscoverWatchdogThread.isAlive()) {
			mDiscoverWatchdogThread = new DiscoverWatchdogThread();
			mDiscoverWatchdogThread.start();
		}
	}

	/**
	 * Broadcast üzenetek figyelésének leállítása
	 */
	public void stopListening() {
		if (mReceiverThread != null && mReceiverThread.isAlive()) {
			mReceiverThread.interrupt();
			try {
				mReceiverThread.join();
			} catch (InterruptedException e) {
			}
		}
		if (mDiscoverWatchdogThread != null && mDiscoverWatchdogThread.isAlive()) {
			mDiscoverWatchdogThread.interrupt();
			try {
				mDiscoverWatchdogThread.join();
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * A hálózaton talált szerverek listájának lekérdezése
	 */
	public ArrayList<InetAddress> getDiscoveredAddresses() {
		ArrayList<InetAddress> addr = new ArrayList<>();
		synchronized (mAddresses) {
			for (Entry<InetAddress, Long> entry : mAddresses.entrySet()) {
				if (entry.getValue() > System.currentTimeMillis() - TIMEOUT) {
					addr.add(entry.getKey());
				}
			}
		}
		return addr;
	}

	/**
	 * A hálózaton talált szerverek listájának ürítése
	 */
	public void clearDiscoveredAddresses() {
		synchronized (mAddresses) {
			mAddresses.clear();
		}
	}

	/**
	 * Új cím hozzáadása a hálózaton talált szerverek listájához
	 * 
	 * @param address
	 */
	private void addInetAddress(InetAddress address) {
		boolean newAddress = false;
		synchronized (mAddresses) {
			long currentTime = System.currentTimeMillis();
			if (!mAddresses.containsKey(address) || mAddresses.get(address) < currentTime - TIMEOUT) {
				newAddress = true;
			}
			mAddresses.put(address, currentTime);
		}

		if (newAddress && mListeners.size() > 0) {
			// Listenerek értesítése
			// Hogy elkerüljük az esetleges deadlock-ot, ha a listener szeretné
			// törölni magát a listából, először kimásoljuk egy ideiglenes
			// listába az összes listenert, és abból értesítjük őket
			ArrayList<NetworkDiscoverListener> listenersTemp = new ArrayList<>();
			synchronized (mListeners) {
				for (NetworkDiscoverListener listener : mListeners) {
					listenersTemp.add(listener);
				}
			}
			for (NetworkDiscoverListener listener : listenersTemp) {
				listener.onDiscover(address, this);
			}
		}
	}

	/**
	 * A broadcast üzenetek periodikus küldését végző Thread
	 */
	private static class BroadcastSenderThread extends Thread {

		private final byte[] mBuffer;
		private DatagramPacket mPacket;
		private DatagramSocket mSocket;

		public BroadcastSenderThread(String magic, int port) throws SocketException {
			mBuffer = magic.getBytes();

			try {
				mPacket = new DatagramPacket(mBuffer, mBuffer.length, InetAddress.getByName("255.255.255.255"), port);
				mSocket = new DatagramSocket();
				mSocket.setBroadcast(true);
			} catch (SocketException e) {
				e.printStackTrace();
				throw e;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			if (mSocket != null) {
				if (mPacket != null) {
					while (true) {
						try {
							mSocket.send(mPacket);
							sleep(1000);
						} catch (InterruptedException e) {
							mSocket.close();
							return;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					mSocket.close();
				}
			}
		}
	}

	/**
	 * A broadcast üzenetekre várakozó Thread
	 */
	private class BroadcastReceiverThread extends Thread {

		private byte[] mBuffer;
		private final String mMagic;
		private DatagramPacket mPacket;
		private DatagramSocket mSocket;

		public BroadcastReceiverThread(String magic, int port) {
			mBuffer = new byte[1024];
			mMagic = magic;
			mPacket = new DatagramPacket(mBuffer, mBuffer.length);
			try {
				mSocket = new DatagramSocket(port);
				mSocket.setBroadcast(true);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void interrupt() {
			super.interrupt();
			if (!mSocket.isClosed()) {
				mSocket.close();
			}
		}

		@Override
		public void run() {
			if (mSocket != null) {
				while (true) {
					try {
						mSocket.receive(mPacket);
						String str = new String(mBuffer, mPacket.getOffset(), mPacket.getLength());
						if (str.contains(mMagic)) {
							addInetAddress(mPacket.getAddress());
						}
					} catch (SocketException e) {
						mSocket.close();
						return;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private class DiscoverWatchdogThread extends Thread {

		@Override
		public void run() {
			List<InetAddress> toRemove = new ArrayList<>();
			while (true) {
				synchronized (mAddresses) {
					for (Entry<InetAddress, Long> entry : mAddresses.entrySet()) {
						if (entry.getValue() < System.currentTimeMillis() - TIMEOUT) {
							InetAddress address = entry.getKey();
							toRemove.add(address);
						}
					}
					for (InetAddress inetAddress : toRemove) {
						mAddresses.remove(inetAddress);
					}
				}
				synchronized (mListeners) {
					for (InetAddress inetAddress : toRemove) {
						for (NetworkDiscoverListener listener : mListeners) {
							listener.onDiscoveredTimeout(inetAddress);
						}
					}
				}
				toRemove.clear();
				try {
					sleep(500);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
}
