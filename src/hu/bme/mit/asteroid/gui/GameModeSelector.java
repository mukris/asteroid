package hu.bme.mit.asteroid.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.sun.istack.internal.logging.Logger;

/**
 * A játék típusának kiválasztását lehetővé tévő Panel
 */
public class GameModeSelector extends JPanel {
	private static final long serialVersionUID = -1709524252970560013L;
	
	private JButton mBtnSingleplayer;
	private JButton mBtnMultiplayer;
	
	public GameModeSelector() {
		mBtnMultiplayer = new JButton("Multiplayer");
		mBtnSingleplayer = new JButton("SinglePlayer");
		
		mBtnMultiplayer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger logger = Logger.getLogger("valami", null);
				logger.info("Katt");
			}
		});
		
		mBtnSingleplayer.setEnabled(false);
		
		add(mBtnMultiplayer);
		add(mBtnSingleplayer);
	}

}
