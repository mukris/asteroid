package hu.bme.mit.asteroid.network;

import java.net.InetAddress;

import hu.bme.mit.asteroid.GameState;
import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.network.NetworkDiscover.NetworkDiscoverListener;
import hu.bme.mit.asteroid.network.NetworkHelper.NetworkConnectionListener;
import hu.bme.mit.asteroid.network.NetworkHelper.NetworkReceiveListener;

/**
 * Minden hálózattal kapcsolatos callback interfészt implementáló osztály. A
 * felhasználás helyén elegendő csak a szükséges metódusokat felüldefiniálni,
 * nem kell foglalkozni az interfész(ek) egyéb, az adott helyen szükségtelen
 * függvényeivel.
 */
public class NetworkListener implements NetworkConnectionListener, NetworkDiscoverListener {

	private final NetworkReceiveListener<GameState> mGameStateListener = new NetworkReceiveListener<GameState>() {
		@Override
		public void onReceive(GameState gameState) {
			NetworkListener.this.onReceive(gameState);
		}
	};

	private final NetworkReceiveListener<ControlEvent> mControlEventListener = new NetworkReceiveListener<ControlEvent>() {
		@Override
		public void onReceive(ControlEvent event) {
			NetworkListener.this.onReceive(event);
		}
	};

	@Override
	public void onConnect() {
	}

	@Override
	public void onDisconnect() {
	}

	/**
	 * Akkor hívódik, ha új {@link GameState} objektum érkezett a szervertől.
	 * 
	 * @param gameState
	 */
	public void onReceive(GameState gameState) {
	}

	/**
	 * Akkor hívódik, ha új {@link ControlEvent} érkezett a klienstől.
	 * 
	 * @param event
	 */
	public void onReceive(ControlEvent event) {
	}

	@Override
	public void onDiscover(InetAddress address, NetworkDiscover networkDiscover) {
	}

	@Override
	public void onDiscoveredTimeout(InetAddress address) {
	}

	public final NetworkReceiveListener<GameState> asGameStateReceiver() {
		return mGameStateListener;
	}

	public final NetworkReceiveListener<ControlEvent> asControlEventReceiver() {
		return mControlEventListener;
	}

}
