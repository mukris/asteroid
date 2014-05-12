package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.Color;

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
					mGameWindow.showPanel(PanelId.GAME_FIELD);
					GameManager.getInstance().startLocalMultiplayerGame();
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
		
		Container multiPLayerButtons = new Container();
		
		multiPLayerButtons.setLayout(new GridLayout(4,0));
		multiPLayerButtons.setSize(10,10);;
		multiPLayerButtons.add(mBtnLocal);
		multiPLayerButtons.add(mBtnServer);
		multiPLayerButtons.add(mBtnClient);
		
		add(multiPLayerButtons);
		
		add(getBackButton(PanelId.GAME_MODE_SELECTOR));
		
		/*
		setLayout(new BorderLayout());
		add(mBtnLocal,BorderLayout.CENTER);
		add(mBtnServer,BorderLayout.CENTER);
		add(mBtnClient,BorderLayout.CENTER);
		add(getBackButton(PanelId.GAME_MODE_SELECTOR));*/
	}
}
