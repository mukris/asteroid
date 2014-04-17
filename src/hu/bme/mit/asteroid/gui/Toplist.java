package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Toplist extends JPanel {

	private static final long serialVersionUID = -8944015110835172602L;

	private GameWindow mGameWindow;
	private JButton mBtnBack;

	public Toplist(GameWindow gameWindow) {
		mGameWindow = gameWindow;

		mBtnBack = new JButton("Vissza");
		mBtnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mGameWindow.showPanel(PanelId.GAME_MODE_SELECTOR);
			}
		});
		add(mBtnBack);
		// TODO Auto-generated constructor stub
	}

}
