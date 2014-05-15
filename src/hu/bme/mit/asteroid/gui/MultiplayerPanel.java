package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * A kétjátékos játékmódok kiválasztását lehetővé tévő panel
 */
public class MultiplayerPanel extends GamePanel {
	private static final long serialVersionUID = -476729603741239671L;

	private JButton mBtnLocal;
	private JButton mBtnServer;
	private JButton mBtnClient;

	public MultiplayerPanel(GameWindow gameWindow) {
		super(gameWindow);

		mBtnLocal = new JButton("Helyi játék");
		mBtnLocal.setFont(mButtonFont);
		mBtnLocal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					mGameWindow.showPanel(PanelId.GAME_FIELD);
					GameManager.getInstance().startLocalMultiplayerGame();
				} catch (LevelNotExistsException e) {
					e.printStackTrace();
				}
			}
		});

		mBtnServer = new JButton("Szerver indítása");
		mBtnServer.setFont(mButtonFont);
		mBtnServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				mGameWindow.showPanel(PanelId.SERVER_PANEL);
			}
		});

		mBtnClient = new JButton("Csatlakozás játékhoz");
		mBtnClient.setFont(mButtonFont);
		mBtnClient.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				mGameWindow.showPanel(PanelId.CLIENT_PANEL);
			}
		});

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.ipadx = 200;
		constraints.weighty = 1;
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.PAGE_START;
		add(getHeaderLabel("Multiplayer"), constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.weighty = 2;
		constraints.weightx = 0.5;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.VERTICAL;
		add(mBtnLocal, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.ipadx = 100;
		add(mBtnServer, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		add(mBtnClient, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.ipady = 30;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.PAGE_END;
		add(getBackButton(PanelId.GAME_MODE_SELECTOR), constraints);
	}
}
