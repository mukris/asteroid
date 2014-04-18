package hu.bme.mit.asteroid.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A hálózati réteg közös elemeit egyesítő absztrakt osztály
 * 
 * @param <ReceiveType>
 *            A várt objektumok típusa
 * @param <SendType>
 *            A küldött objektumok típusa
 */
public abstract class NetworkHelper<ReceiveType, SendType> {

	protected static final int PORT = 8766;

	protected Socket mClientSocket;
	private ObjectInputStream mInput;
	private ObjectOutputStream mOutput;
	private ReceiverThread<ReceiveType> mReceiverThread;
	protected ArrayList<NetworkConnectionListener> mConnectionListeners = new ArrayList<>();
	protected ArrayList<NetworkReceiveListener<ReceiveType>> mReceiveListeners = new ArrayList<>();

	/**
	 * A hálózati kapcsolat állapotváltozásait jelző interfész.
	 */
	public interface NetworkConnectionListener {
		/**
		 * Akkor hívódik, ha sikerült csatlakozni a másik játékoshoz.
		 */
		void onConnect();

		/**
		 * Akkor hívódik, ha megszakadt a kapcsolat a másik játékossal.
		 */
		void onDisconnect();
	}

	/**
	 * A hálózati kapcsolaton új adat beérkezését jelző interfész.
	 * 
	 * @param <ReceiveType>
	 *            A várt objektum típusa
	 */
	public interface NetworkReceiveListener<ReceiveType> {
		/**
		 * Akkor hívódik, ha új objektum érkezett a hálózaton.
		 * 
		 * @param data
		 *            A beérkezett objektum
		 */
		void onReceive(ReceiveType data);
	}

	public void addConnectionListener(NetworkConnectionListener listener) throws NullPointerException {
		if (listener == null) {
			throw new NullPointerException();
		}
		synchronized (mConnectionListeners) {
			mConnectionListeners.add(listener);
		}
	}

	public void removeConnectionListener(NetworkConnectionListener listener) {
		synchronized (mConnectionListeners) {
			mConnectionListeners.remove(listener);
		}
	}

	public void addReceiveListener(NetworkReceiveListener<ReceiveType> listener) throws NullPointerException {
		if (listener == null) {
			throw new NullPointerException();
		}
		synchronized (mReceiveListeners) {
			mReceiveListeners.add(listener);
		}
	}

	public void removeReceiveListener(NetworkReceiveListener<ReceiveType> listener) {
		synchronized (mReceiveListeners) {
			mReceiveListeners.remove(listener);
		}
	}

	/**
	 * A megadott típusú adatok fogadásáért felelős Thread
	 * 
	 * @param <ReceiveType>
	 *            A várt adatok típusa
	 */
	@SuppressWarnings("hiding")
	protected class ReceiverThread<ReceiveType> extends Thread {

		private AtomicBoolean mRunning = new AtomicBoolean(true);
		private ArrayList<NetworkReceiveListener<ReceiveType>> mListeners;

		public ReceiverThread(ArrayList<NetworkReceiveListener<ReceiveType>> listeners) {
			mListeners = listeners;
		}

		@Override
		public void run() {
			if (mInput == null) {
				return;
			}
			while (mRunning.get()) {
				try {
					if (mInput.available() > 0) {
						if (Thread.interrupted()) {
							throw new InterruptedException();
						}
						@SuppressWarnings("unchecked")
						ReceiveType readObject = (ReceiveType) mInput.readObject();
						if (mListeners != null) {
							synchronized (NetworkHelper.this.mReceiveListeners) {
								for (NetworkReceiveListener<ReceiveType> listener : mListeners) {
									listener.onReceive(readObject);
								}
							}
						}
					} else {
						sleep(10);
					}
				} catch (InterruptedException e) {
					return;
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Stopped receiving data from the network.");
					return;
				}
			}
		}

		public void setRunning(boolean run) {
			mRunning.set(run);
		}
	}

	protected void initCommunication() throws IOException {
		mOutput = new ObjectOutputStream(mClientSocket.getOutputStream());
		mOutput.flush();
		mInput = new ObjectInputStream(mClientSocket.getInputStream());
	}

	/**
	 * A {@link ReceiverThread} indítása.
	 */
	protected void startReceiving() {
		mReceiverThread = new ReceiverThread<>(mReceiveListeners);
		mReceiverThread.start();
	}

	/**
	 * A {@link ReceiverThread} leállítása.
	 */
	protected void stopReceiving() {
		if (mReceiverThread != null) {
			try {
				mReceiverThread.setRunning(false);
				mReceiverThread.interrupt();
				mReceiverThread.join();
				mReceiverThread = null;
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Kapcsolat bontása. Meghívása kötelező az erőforrások felszabadítása
	 * érdekben.
	 */
	protected void disconnect() {
		try {
			stopReceiving();

			if (mInput != null)
				mInput.close();
			if (mOutput != null)
				mOutput.close();
			if (mClientSocket != null)
				mClientSocket.close();
			if (mConnectionListeners != null) {
				synchronized (mConnectionListeners) {
					for (NetworkConnectionListener listener : mConnectionListeners) {
						listener.onDisconnect();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adat küldése a másik gépnek
	 * 
	 * @param data
	 *            A küldendő objektum
	 * @return Sikeres küldés esetén true, egyébként false
	 */
	protected boolean send(SendType data) {
		if (mClientSocket == null || mOutput == null) {
			return false;
		}
		try {
			mOutput.writeObject(data);
			mOutput.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
