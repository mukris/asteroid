package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.Storage;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.model.ToplistItem;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;

/**
 * A toplista megjelenítését végző panel
 */
public class Toplist extends GamePanel {
	private static final long serialVersionUID = 2671114035095351953L;

	private Container mToplistTable = new Container();
	private final JLabel mLabelHeaderRank;
	private final JLabel mLabelHeaderName;
	private final JLabel mLabelHeaderScore;

	public Toplist(GameWindow gameWindow) {
		super(gameWindow);

		mLabelHeaderRank = new JLabel("Helyezés");
		mLabelHeaderRank.setFont(mHeaderFont);
		mLabelHeaderRank.setForeground(Color.WHITE);
		mLabelHeaderRank.setHorizontalAlignment(JLabel.CENTER);

		mLabelHeaderName = new JLabel("Név");
		mLabelHeaderName.setFont(mHeaderFont);
		mLabelHeaderName.setForeground(Color.WHITE);
		mLabelHeaderName.setHorizontalAlignment(JLabel.CENTER);

		mLabelHeaderScore = new JLabel("Pontszám");
		mLabelHeaderScore.setFont(mHeaderFont);
		mLabelHeaderScore.setForeground(Color.WHITE);
		mLabelHeaderScore.setHorizontalAlignment(JLabel.CENTER);

		update(mToplistTable);

		mToplistTable.setLayout(new GridLayout(11, 3));

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.ipadx = 200;
		constraints.weighty = 1;
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.PAGE_START;
		add(getHeaderLabel("Toplista"), constraints);

		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(15, 30, 30, 0);
		add(mToplistTable, constraints);

		constraints.gridy = 2;
		constraints.ipady = 30;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.PAGE_END;
		constraints.insets = new Insets(0, 30, 30, 30);
		add(getBackButton(PanelId.GAME_MODE_SELECTOR), constraints);
	}

	@Override
	protected void onShow() {
		super.onShow();
		update(mToplistTable);
	}

	private void update(Container toplistTable) {
		final ArrayList<ToplistItem> toplistItems = Storage.getInstance().getToplistItems();
		Collections.reverse(toplistItems);

		toplistTable.removeAll();

		mToplistTable.add(mLabelHeaderRank);
		mToplistTable.add(mLabelHeaderName);
		mToplistTable.add(mLabelHeaderScore);

		int i = 1;
		for (ToplistItem toplistItem : toplistItems) {
			final JLabel labelRank = new JLabel(i++ + ".");
			labelRank.setHorizontalAlignment(JLabel.CENTER);
			labelRank.setFont(mStandardFont);
			labelRank.setForeground(Color.WHITE);
			mToplistTable.add(labelRank);

			final JLabel labelName = new JLabel(toplistItem.getName());
			labelName.setHorizontalAlignment(JLabel.CENTER);
			labelName.setFont(mStandardFont);
			labelName.setForeground(Color.WHITE);
			mToplistTable.add(labelName);

			final JLabel labelScore = new JLabel(toplistItem.getPoints() + "");
			labelScore.setHorizontalAlignment(JLabel.CENTER);
			labelScore.setFont(mStandardFont);
			labelScore.setForeground(Color.WHITE);
			mToplistTable.add(labelScore);
		}
		toplistTable.revalidate();
		repaint();
	}
}
