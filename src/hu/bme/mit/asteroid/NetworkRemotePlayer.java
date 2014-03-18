package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.network.NetworkServer.NetworkServerListener;

/**
 * Egy hálózaton játszó távoli játékost reprezentáló osztály
 */
public class NetworkRemotePlayer extends Player implements NetworkServerListener {

	public NetworkRemotePlayer() {
		super(null);
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceive(ControlEvent event) {
		control(event);
	}
}
