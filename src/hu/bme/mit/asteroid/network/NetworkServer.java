package hu.bme.mit.asteroid.network;

import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.control.ControlEvent;

import java.io.IOException;
import java.net.ServerSocket;

public class NetworkServer extends NetworkHelper<ControlEvent, GameState> {

	public interface NetworkServerListener extends NetworkListener<ControlEvent> {
	}

	private AcceptThread mAcceptThread;

	public NetworkServer(NetworkServerListener listener) {
		mListener = listener;
	}

	public void startListening() throws IOException {
		mAcceptThread = new AcceptThread(PORT);
		mAcceptThread.start();
	}

	public void stopListening() {
		if (mAcceptThread != null) {
			mAcceptThread.close();
			try {
				mAcceptThread.join();
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void disconnect() {
		super.disconnect();
		if (mListener != null) {
			mListener.onDisconnect();
		}
	}

	public void sendGameState(GameState gameState) {
		super.send(gameState);
	}

	private void onConnect() {
		if (mListener != null) {
			mListener.onConnect();
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

	private class AcceptThread extends Thread {

		private ServerSocket mServerSocket;
		private int mTimeout;

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
				e.printStackTrace();
			}
		}

		public void close() {
			try {
				if (!mServerSocket.isClosed()) {
					mServerSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
