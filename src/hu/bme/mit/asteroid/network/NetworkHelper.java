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
abstract class NetworkHelper<ReceiveType, SendType> {

	protected static final int PORT = 8766;

	protected Socket mClientSocket;
	private ObjectInputStream mInput;
	private ObjectOutputStream mOutput;
	private ReceiverThread<ReceiveType> mReceiverThread;
	protected ArrayList<NetworkListener<ReceiveType>> mListeners;

	/**
	 * A hálózati kapcsolat főbb eseményeinek lekezelését lehetővé tévő
	 * interfész.
	 * 
	 * @param <ReceiveType>
	 *            A várt objektum típusa
	 */
	protected interface NetworkListener<ReceiveType> {
		/**
		 * Akkor hívódik, ha sikerült csatlakozni a másik játékoshoz.
		 */
		void onConnect();

		/**
		 * Akkor hívódik, ha megszakadt a kapcsolat a másik játékossal.
		 */
		void onDisconnect();

		/**
		 * Akkor hívódik, ha új objektum érkezett a hálózaton.
		 * 
		 * @param data
		 *            A beérkezett objektum
		 */
		void onReceive(ReceiveType data);
	}

	public void addListener(NetworkListener<ReceiveType> listener) throws NullPointerException {
		if (listener == null) {
			throw new NullPointerException();
		}
		synchronized (mListeners) {
			mListeners.add(listener);
		}
	}

	public void removeListener(NetworkListener<ReceiveType> listener) {
		synchronized (mListeners) {
			mListeners.remove(listener);
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

		private AtomicBoolean mRunning;
		private ArrayList<NetworkListener<ReceiveType>> mListeners;

		public ReceiverThread(ArrayList<NetworkListener<ReceiveType>> listeners) {
			mRunning.set(true);
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
							synchronized (NetworkHelper.this.mListeners) {
								for (NetworkListener<ReceiveType> listener : mListeners) {
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
		mInput = new ObjectInputStream(mClientSocket.getInputStream());
		mOutput = new ObjectOutputStream(mClientSocket.getOutputStream());
		mOutput.flush();
	}

	/**
	 * A {@link ReceiverThread} indítása.
	 */
	protected void startReceiving() {
		mReceiverThread = new ReceiverThread<>(mListeners);
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
			if (mListeners != null) {
				synchronized (mListeners) {
					for (NetworkListener<ReceiveType> listener : mListeners) {
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
