package hu.bme.mit.asteroid.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A hálózati szerver felderítésért felelős osztály
 */
public class NetworkDiscover {

	private static final String MAGIC_STRING = "### Asteroid game magic string ###";
	private static final int PORT = 8765;
	private BroadcastSenderThread mSenderThread;
	private BroadcastReceiverThread mReceiverThread;
	private Set<InetAddress> mAddresses;

	public NetworkDiscover() {
		mAddresses = new HashSet<>();
	}

	/**
	 * Broadcast üzenetek periodikus küldésének indítása
	 * @throws SocketException 
	 */
	public void startBroadcasting() throws SocketException {
		mSenderThread = new BroadcastSenderThread(MAGIC_STRING, PORT);
		mSenderThread.start();
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
		mReceiverThread = new BroadcastReceiverThread(MAGIC_STRING, PORT);
		mReceiverThread.start();
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
	}

	/**
	 * A hálózaton talált szerverek listájának lekérdezése
	 */
	public synchronized ArrayList<InetAddress> getDiscoveredAddresses() {
		ArrayList<InetAddress> addr = new ArrayList<>();
		for (InetAddress inetAddress : mAddresses) {
			addr.add(inetAddress);
		}
		return addr;
	}

	/**
	 * A hálózaton talált szerverek listájának ürítése
	 */
	public synchronized void clearDiscoveredAddresses() {
		mAddresses.clear();
	}

	/**
	 * Új cím hozzáadása a hálózaton talált szerverek listájához
	 * 
	 * @param address
	 */
	private synchronized void addInetAddress(InetAddress address) {
		mAddresses.add(address);
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
			if(!mSocket.isClosed()) {
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
}
