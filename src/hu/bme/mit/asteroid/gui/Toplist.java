package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

public class Toplist extends GamePanel {

	private static final long serialVersionUID = -8944015110835172602L;

	public Toplist(GameWindow gameWindow) {
		super(gameWindow);
		
		add(getBackButton(PanelId.GAME_MODE_SELECTOR));
		// TODO Auto-generated constructor stub
	}

}
