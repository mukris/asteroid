package hu.bme.mit.asteroid.player;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.network.NetworkHelper.NetworkReceiveListener;

/**
 * Egy hálózaton játszó távoli játékost reprezentáló osztály
 */
public class NetworkRemotePlayer extends Player implements NetworkReceiveListener<ControlEvent> {

	public NetworkRemotePlayer() {
		super(null);
	}

	@Override
	public void onReceive(ControlEvent event) {
		control(event);
	}
}
