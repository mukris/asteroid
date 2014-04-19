package hu.bme.mit.asteroid.network;

import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.control.ControlEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <p>
 * Az osztály a játék hálózati interfészének kliens oldalát valósítja meg.
 * </p>
 * <p>
 * A távoli gépen futó szervernek a {@link ControlEvent} osztály által
 * reprezentált vezérlőutasításokat küld, és a játék aktuális állapotát tároló
 * {@link GameState} osztályt fogad a szervertől.
 * </p>
 */
public class NetworkClient extends NetworkHelper<GameState, ControlEvent> {

	private static final int TIMEOUT = 3000;

	/**
	 * A hálózati kapcsolat főbb kliensoldali eseményeinek lekezelését lehetővé
	 * tévő interfész.
	 */
	public interface NetworkClientListener extends NetworkReceiveListener<GameState>, NetworkConnectionListener {
	}

	public NetworkClient() {
		mClientSocket = new Socket();
	}

	public NetworkClient(NetworkClientListener listener) throws NullPointerException {
		this();
		addConnectionListener(listener);
		addReceiveListener(listener);
	}

	public NetworkClient(NetworkReceiveListener<GameState> listener) throws NullPointerException {
		this();
		addReceiveListener(listener);
	}

	public NetworkClient(NetworkConnectionListener listener) throws NullPointerException {
		this();
		addConnectionListener(listener);
	}

	/**
	 * Kapcsolódik a paraméterben kapott IP címre. A kapcsolat létrejöttét a
	 * {@link NetworkClientListener} interfész
	 * {@link NetworkClientListener#onConnect()} függvény meghívása jelzi.
	 * Sikertelen kapcsolódás, vagy idő túllépés esetén a
	 * {@link NetworkClientListener#onDisconnect()} függvény hívódik.
	 * 
	 * @param address
	 *            A cím, amihez kapcsolódni szeretnénk
	 * @throws IOException
	 */
	public void connect(InetAddress address) throws IOException {
		try {
			mClientSocket.connect(new InetSocketAddress(address, PORT), TIMEOUT);
			initCommunication();

			if (mReceiveListeners != null) {
				synchronized (mReceiveListeners) {
					for (NetworkConnectionListener listener : mConnectionListeners) {
						listener.onConnect();
					}
				}
			}

			startReceiving();
		} catch (IOException e) {
			disconnect();
			throw e;
		}
	}

	/**
	 * Vezérlőutasítások küldése a szervernek
	 * 
	 * @param controlEvent
	 * @return Sikeres küldés esetén true, egyébként false
	 */
	public boolean sendControlEvent(ControlEvent controlEvent) {
		boolean success = send(controlEvent);

		if (!success) {
			disconnect();
		}
		return success;
	}
}
