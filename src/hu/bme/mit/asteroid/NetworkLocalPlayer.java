package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlEvent;
import hu.bme.mit.asteroid.control.ControlInterface;
import hu.bme.mit.asteroid.network.NetworkClient;

/**
 * Egy hálózaton játszó helyi játékost reprezentáló osztály
 */
public class NetworkLocalPlayer extends Player {

	private NetworkClient mNetworkClient;
	
	public NetworkLocalPlayer(ControlInterface controlInterface, NetworkClient networkClient) {
		super(controlInterface);
		mNetworkClient = networkClient;
	}
	
	@Override
	public void control(ControlEvent event) {
		if(mNetworkClient != null){
			mNetworkClient.sendControlEvent(event);
		}
	}
}
