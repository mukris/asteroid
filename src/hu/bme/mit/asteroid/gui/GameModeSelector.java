package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * A játék típusának kiválasztását lehetővé tévő Panel
 */
public class GameModeSelector extends GamePanel {
	private static final long serialVersionUID = -1709524252970560013L;

	private JButton mBtnSingleplayer;
	private JButton mBtnMultiplayer;

	public GameModeSelector(GameWindow gameWindow) {
		super(gameWindow);
		mBtnMultiplayer = new JButton("Multiplayer");
		mBtnSingleplayer = new JButton("SinglePlayer");

		mBtnMultiplayer.setFont(mButtonFont);
		mBtnSingleplayer.setFont(mButtonFont);

		mBtnSingleplayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mGameWindow.showPanel(PanelId.LEVEL_SELECTOR);
			}
		});

		mBtnMultiplayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mGameWindow.showPanel(PanelId.MULTIPLAYER_PANEL);
			}
		});

		add(getHeaderLabel("Asteriod Shooter"));
		add(mBtnSingleplayer);
		add(mBtnMultiplayer);
	}
}
