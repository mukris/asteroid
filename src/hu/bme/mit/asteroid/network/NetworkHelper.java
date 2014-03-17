package hu.bme.mit.asteroid.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

abstract class NetworkHelper<ReceiveType, SendType> {

	protected static final int PORT = 8766;

	protected Socket mClientSocket;
	private ObjectInputStream mInput;
	private ObjectOutputStream mOutput;
	private ReceiverThread<ReceiveType> mReceiverThread;
	protected NetworkListener<ReceiveType> mListener;

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

	@SuppressWarnings("hiding")
	protected class ReceiverThread<ReceiveType> extends Thread {

		private AtomicBoolean mRunning;
		private NetworkListener<ReceiveType> mListener;

		public ReceiverThread(NetworkListener<ReceiveType> listener) {
			mRunning.set(true);
			mListener = listener;
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
						ReceiveType readObject = (ReceiveType) mInput.readObject();
						if (mListener != null) {
							mListener.onReceive(readObject);
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

	protected void startReceiving() {
		mReceiverThread = new ReceiverThread<>(mListener);
		mReceiverThread.start();
	}

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

	protected void disconnect() {
		try {
			stopReceiving();

			if (mInput != null)
				mInput.close();
			if (mOutput != null)
				mOutput.close();
			if (mClientSocket != null)
				mClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
