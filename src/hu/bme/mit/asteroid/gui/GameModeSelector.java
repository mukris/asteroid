package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * A játék típusának kiválasztását lehetővé tévő Panel
 */
public class GameModeSelector extends GamePanel {
	private static final long serialVersionUID = -3824933256090390015L;

	private JButton mBtnSingleplayer;
	private JButton mBtnMultiplayer;

	public GameModeSelector(GameWindow gameWindow) {
		super(gameWindow);
		mBtnMultiplayer = new JButton("Multiplayer");
		mBtnSingleplayer = new JButton("Singleplayer");

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

		setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.weighty = 1;
		constraints.anchor = GridBagConstraints.PAGE_START;
		add(getHeaderLabel("Asteriod Shooter"), constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 0.5;
		constraints.ipadx = 200;
		constraints.ipady = 200;
		add(mBtnSingleplayer, constraints);

		constraints.gridx = 1;
		add(mBtnMultiplayer, constraints);
	}
}
