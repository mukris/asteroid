package hu.bme.mit.asteroid.network;

import java.net.InetAddress;

import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.network.NetworkDiscover.NetworkDiscoverListener;
import hu.bme.mit.asteroid.network.NetworkHelper.NetworkConnectionListener;
import hu.bme.mit.asteroid.network.NetworkHelper.NetworkReceiveListener;

public class NetworkListener implements NetworkConnectionListener, NetworkReceiveListener<Object>,
		NetworkDiscoverListener {

	private final NetworkReceiveListener<GameState> mGameStateListener = new NetworkReceiveListener<GameState>() {
		@Override
		public void onReceive(GameState gameState) {
			onReceive(gameState);
		}
	};

	private final NetworkReceiveListener<ControlEvent> mControlEventListener = new NetworkReceiveListener<ControlEvent>() {
		@Override
		public void onReceive(ControlEvent event) {
			onReceive(event);
		}
	};

	@Override
	public void onConnect() {
	}

	@Override
	public void onDisconnect() {
	}

	public void onReceive(GameState data) {
	}

	public void onReceive(ControlEvent data) {
	}

	@Override
	public void onReceive(Object data) {
		if (data instanceof ControlEvent) {
			onReceive(data);
		} else if (data instanceof GameState) {
			onReceive(data);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void onDiscover(InetAddress address, NetworkDiscover networkDiscover) {
	}

	public final NetworkReceiveListener<GameState> asGameStateReceiver() {
		return mGameStateListener;
	}

	public final NetworkReceiveListener<ControlEvent> asControlEventReceiver() {
		return mControlEventListener;
	}

}
