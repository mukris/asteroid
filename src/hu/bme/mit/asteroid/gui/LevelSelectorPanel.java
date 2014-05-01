package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameFactory;
import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.Storage;
import hu.bme.mit.asteroid.exceptions.LevelNotLoadableException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Az egyes pályák kiválasztását lehetővé tévő Panel
 */
public class LevelSelectorPanel extends GamePanel {
	private static final long serialVersionUID = -3931243134452873491L;

	private JButton mBtnToplist;
	private List<JButton> mLevelButtons;
	private ActionListener mLevelButtonListener;

	public LevelSelectorPanel(GameWindow gameWindow) {
		super(gameWindow);

		mBtnToplist = new JButton("Toplista");
		mBtnToplist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mGameWindow.showPanel(PanelId.TOPLIST);
			}
		});

		add(mBtnToplist);
		add(getBackButton(PanelId.GAME_MODE_SELECTOR));

		mLevelButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JButton button = (JButton) event.getSource();
				try {
					mGameWindow.showPanel(PanelId.GAME_FIELD);
					GameManager.getInstance().startSinglePlayerGame(Integer.parseInt(button.getText()) - 1);
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
		updateLevelUnlockStatus();
	}

	@Override
	protected void onShow() {
		super.onShow();
		updateLevelUnlockStatus();
	}

	/**
	 * A pályák feloldottsági állapotának frissítése
	 */
	private void updateLevelUnlockStatus() {
		int highestUnlockedLevel = Storage.getInstance().getHighestUnlockedLevel();
		for (int i = 0; i < mLevelButtons.size(); i++) {
			mLevelButtons.get(i).setEnabled(i <= highestUnlockedLevel);
		}
	}
}
