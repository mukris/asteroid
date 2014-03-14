package hu.bme.mit.asteroid;

import hu.bme.mit.asteroid.control.ControlInterface;

/**
 * Egy hálózaton játszó helyi játékost reprezentáló osztály
 */
public class NetworkLocalPlayer extends Player {

	public NetworkLocalPlayer(ControlInterface controlInterface) {
		super(controlInterface);
	}
}
