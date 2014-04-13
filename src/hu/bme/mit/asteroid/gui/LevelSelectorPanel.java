package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Az egyes pályák kiválasztását lehetővé tévő Panel 
 */
public class LevelSelectorPanel extends JPanel {
	private static final long serialVersionUID = -8459751887544384881L;
	
	private GameWindow mGameWindow;
	private JButton mBtnBack;
	
	public LevelSelectorPanel(GameWindow gameWindow) {
		mGameWindow = gameWindow;
		
		mBtnBack = new JButton("Vissza");
		mBtnBack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mGameWindow.showPanel(PanelId.GAME_MODE_SELECTOR);
			}
		});
		
		add(mBtnBack);
		
		// TODO Szinteknek megfelelő gombok
	}

}
