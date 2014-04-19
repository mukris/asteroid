package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * A kétjátékos játékmódok kiválasztását lehetővé tévő panel
 */
public class MultiplayerPanel extends GamePanel {
	private static final long serialVersionUID = 8978172251199097146L;

	private JButton mBtnLocal;
	private JButton mBtnServer;
	private JButton mBtnClient;

	public MultiplayerPanel(GameWindow gameWindow) {
		super(gameWindow);
		mBtnLocal = new JButton("Helyi játék");
		mBtnLocal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					GameManager.getInstance().startLocalMultiplayerGame();
					mGameWindow.showPanel(PanelId.GAME_FIELD);
				} catch (LevelNotExistsException e) {
					e.printStackTrace();
				}
			}
		});

		mBtnServer = new JButton("Szerver indítása");
		mBtnServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				mGameWindow.showPanel(PanelId.SERVER_PANEL);
			}
		});

		mBtnClient = new JButton("Csatlakozás játékhoz");
		mBtnClient.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				mGameWindow.showPanel(PanelId.CLIENT_PANEL);
			}
		});

		add(mBtnLocal);
		add(mBtnServer);
		add(mBtnClient);
		add(getBackButton(PanelId.GAME_MODE_SELECTOR));
	}
}
