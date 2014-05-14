package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameFactory;
import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.Storage;
import hu.bme.mit.asteroid.exceptions.LevelNotLoadableException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
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
		mBtnToplist.setFont(mButtonFont);
		mBtnToplist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mGameWindow.showPanel(PanelId.TOPLIST);
			}
		});

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

		JLabel labelLevels = new JLabel("Pályák");
		labelLevels.setFont(mStandardFont);
		labelLevels.setHorizontalAlignment(JLabel.CENTER);

		mLevelButtons = new ArrayList<>();
		Container levelButtons = new Container();

		GridLayout gridLayout = new GridLayout(2, 5);
		gridLayout.setHgap(10);
		gridLayout.setVgap(10);
		levelButtons.setLayout(gridLayout);

		for (int i = 0; i < GameFactory.getLevelCount(); i++) {
			JButton button = new JButton("" + (i + 1));
			button.setFont(mButtonFont);
			button.addActionListener(mLevelButtonListener);
			mLevelButtons.add(button);
			levelButtons.add(button);

		}
		updateLevelUnlockStatus();

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.ipadx = 200;
		constraints.weighty = 1;
		constraints.anchor = GridBagConstraints.PAGE_START;
		add(getHeaderLabel("Singleplayer"), constraints);

		constraints.gridy = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		add(labelLevels, constraints);

		constraints.gridy = 2;
		constraints.weighty = 4;
		constraints.fill = GridBagConstraints.BOTH;
		add(levelButtons, constraints);

		constraints.gridy = 3;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.PAGE_END;
		add(mBtnToplist, constraints);

		constraints.gridy = 4;
		constraints.ipady = 30;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(getBackButton(PanelId.GAME_MODE_SELECTOR), constraints);
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
