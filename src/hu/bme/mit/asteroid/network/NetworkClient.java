package hu.bme.mit.asteroid.network;

import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.control.ControlEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkClient extends NetworkHelper<GameState, ControlEvent> {

	private static final int TIMEOUT = 3000;

	public interface NetworkClientListener extends NetworkListener<GameState> {
	}

	public NetworkClient(NetworkClientListener listener) {
		mListener = listener;
		mClientSocket = new Socket();
	}

	public void connect(InetAddress address) throws IOException {
		try {
			mClientSocket.connect(new InetSocketAddress(address, PORT), TIMEOUT);
			initCommunication();

			if (mListener != null) {
				mListener.onConnect();
			}

			startReceiving();
		} catch (IOException e) {
			disconnect();
			throw e;
		}
	}

	@Override
	public void disconnect() {
		super.disconnect();
		if (mListener != null) {
			mListener.onDisconnect();
		}
	}

	public boolean sendControlEvent(ControlEvent controlEvent) {
		return send(controlEvent);
	}
}
