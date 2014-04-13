package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;


/**
 * A játék típusának kiválasztását lehetővé tévő Panel
 */
public class GameModeSelector extends JPanel {
	private static final long serialVersionUID = -1709524252970560013L;

	private final GameWindow mGameWindow;
	private JButton mBtnSingleplayer;
	private JButton mBtnMultiplayer;
	
	public GameModeSelector(GameWindow gameWindow) {
		mGameWindow = gameWindow;
		mBtnMultiplayer = new JButton("Multiplayer");
		mBtnSingleplayer = new JButton("SinglePlayer");
		
		mBtnSingleplayer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mGameWindow.showPanel(PanelId.LEVEL_SELECTOR);
			}
		});
		
		mBtnMultiplayer.setEnabled(false);
		
		add(mBtnSingleplayer);
		add(mBtnMultiplayer);
	}

}
