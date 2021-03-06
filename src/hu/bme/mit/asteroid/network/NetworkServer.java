package hu.bme.mit.asteroid.network;

import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.control.ControlEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>
 * Az osztály a játék hálózati interfészének szerver oldalát valósítja meg.
 * </p>
 * <p>
 * A távoli gépen futó klienstől a {@link ControlEvent} osztály által
 * reprezentált vezérlőutasításokat fogadja és továbbítja a rendszer többi része
 * felé feldolgozásra.
 * </p>
 * <p>
 * A játék aktuális állapotát tároló {@link GameState} osztályt küldi a kliens
 * felé.
 * </p>
 */
public class NetworkServer extends NetworkHelper<ControlEvent, GameState> {

	/**
	 * A hálózati kapcsolat főbb szerveroldali eseményeinek lekezelését lehetővé
	 * tévő interfész.
	 */
	public interface NetworkServerListener extends NetworkReceiveListener<ControlEvent>, NetworkConnectionListener {
	}

	private AcceptThread mAcceptThread;

	public NetworkServer() {

	}

	public NetworkServer(NetworkServerListener listener) throws NullPointerException {
		addConnectionListener(listener);
		addReceiveListener(listener);
	}

	public NetworkServer(NetworkConnectionListener listener) throws NullPointerException {
		addConnectionListener(listener);
	}

	public NetworkServer(NetworkReceiveListener<ControlEvent> listener) throws NullPointerException {
		addReceiveListener(listener);
	}

	public void addNetworkServerListener(NetworkServerListener listener) throws NullPointerException {
		addConnectionListener(listener);
		addReceiveListener(listener);
	}

	public void removeNetworkServerListener(NetworkServerListener listener) {
		removeConnectionListener(listener);
		removeReceiveListener(listener);
	}

	/**
	 * Elindítja a szervert, ami elkezd várakozni a kliens kapcsolódására. A
	 * kapcsolat létrejöttét a {@link NetworkServerListener} interfész
	 * {@link NetworkServerListener#onConnect()} függvény meghívása jelzi.
	 * 
	 * @throws IOException
	 *             Foglalt port, vagy egyéb hálózati hiba esetén keletkezhet
	 *             kivétel
	 */
	public void startListening() throws IOException {
		mAcceptThread = new AcceptThread(PORT);
		mAcceptThread.start();
	}

	/**
	 * A szerver leállítása. Ezek után nem várakozik a kliens kapcsolódására.
	 */
	public void stopListening() {
		if (mAcceptThread != null) {
			mAcceptThread.interrupt();
			try {
				mAcceptThread.join();
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Játékállapot küldése a kliensnek
	 * 
	 * @param gameState
	 * @return Sikeres küldés esetén true, egyébként false
	 */
	public boolean sendGameState(GameState gameState) {
		boolean success = super.send(gameState);

		if (!success) {
			disconnect();
		}
		return success;
	}

	/**
	 * Visszaadja a számítógép összes ismert IP címét
	 * 
	 * @return Az IP címek listája
	 */
	public List<InetAddress> getOwnIPs() {
		ArrayList<InetAddress> addresses = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration<InetAddress> ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress address = (InetAddress) ee.nextElement();
					addresses.add(address);
				}
			}
		} catch (SocketException e) {
		}
		return addresses;
	}

	private void onConnect() {
		if (mReceiveListeners != null) {
			synchronized (mReceiveListeners) {
				for (NetworkConnectionListener listener : mConnectionListeners) {
					listener.onConnect();
				}
			}
		}
		startReceiving();
		if (mAcceptThread != null) {
			try {
				mAcceptThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A kliens kapcsolódására várakozó Thread
	 */
	private class AcceptThread extends Thread {

		private ServerSocket mServerSocket;
		private int mTimeout;
		private boolean mIsClosing = false;

		public AcceptThread(int port) throws IOException {
			this(port, 0);
		}

		public AcceptThread(int port, int timeout) throws IOException {
			mServerSocket = new ServerSocket(port);
			mTimeout = timeout;
		}

		@Override
		public void run() {
			try {
				mServerSocket.setSoTimeout(mTimeout);
				mClientSocket = mServerSocket.accept();
				initCommunication();
				onConnect();
			} catch (IOException e) {
				if (mIsClosing) {
					return;
				} else {
					e.printStackTrace();
				}
			} finally {
				if (!mIsClosing) {
					close();
				}
			}
		}

		@Override
		public void interrupt() {
			super.interrupt();
			close();
		}

		private void close() {
			try {
				mIsClosing = true;
				if (!mServerSocket.isClosed()) {
					mServerSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
