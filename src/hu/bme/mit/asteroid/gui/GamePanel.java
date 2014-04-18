package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A kezelőfelület paneljeinek közös őse
 */
public abstract class GamePanel extends JPanel {
	private static final long serialVersionUID = 6984627964669987283L;

	protected final GameWindow mGameWindow;
	protected JButton mBtnBack;

	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	public GamePanel(GameWindow gameWindow) {
		mGameWindow = gameWindow;
	}

	protected JButton getBackButton(final PanelId panelId) {
		mBtnBack = new JButton("Vissza");
		mBtnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				mGameWindow.showPanel(panelId);
			}
		});
		return mBtnBack;
	}

	/**
	 * Akkor hívódik, amikor a panel megjelenik
	 */
	protected void onShow() {

	}

	/**
	 * Akkor hívódik, amikor a panel helyére másik kerül
	 */
	protected void onHide() {

	}

}
