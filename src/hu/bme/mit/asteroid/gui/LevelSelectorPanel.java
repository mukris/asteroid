package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameFactory;
import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.exceptions.LevelNotLoadableException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Az egyes pályák kiválasztását lehetővé tévő Panel
 */
public class LevelSelectorPanel extends JPanel {
	private static final long serialVersionUID = -8459751887544384881L;

	private GameWindow mGameWindow;
	private JButton mBtnToplist;
	private JButton mBtnBack;
	private List<JButton> mLevelButtons;
	private ActionListener mLevelButtonListener;

	public LevelSelectorPanel(GameWindow gameWindow) {
		mGameWindow = gameWindow;

		mBtnBack = new JButton("Vissza");
		mBtnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mGameWindow.showPanel(PanelId.GAME_MODE_SELECTOR);
			}
		});

		mBtnToplist = new JButton("Toplista");
		mBtnToplist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mGameWindow.showPanel(PanelId.TOPLIST);
			}
		});

		add(mBtnToplist);
		add(mBtnBack);

		mLevelButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JButton button = (JButton) event.getSource();
				try {
					GameManager.getInstance().startSinglePlayerGame(Integer.parseInt(button.getText()) - 1);
					mGameWindow.showPanel(PanelId.GAME_FIELD);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (LevelNotLoadableException e) {
					JOptionPane.showMessageDialog(LevelSelectorPanel.this, e.getMessage(), "A pálya nem betölthető",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};

		mLevelButtons = new ArrayList<>();

		for (int i = 0; i < GameFactory.getLevelCount(); i++) {
			JButton button = new JButton("" + (i + 1));
			button.addActionListener(mLevelButtonListener);
			mLevelButtons.add(button);
			add(button);
		}

		// TODO Feloldatlan pályához tartozó gombok letiltása
	}

}
