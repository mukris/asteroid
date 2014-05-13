package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.Storage;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.model.ToplistItem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;

/**
 * A toplista megjelenítését végző panel
 */
public class Toplist extends GamePanel {
	private static final long serialVersionUID = -2252378497305833635L;

	private Container mToplistTable = new Container();
	private final JLabel mLabelHeaderRank;
	private final JLabel mLabelHeaderName;
	private final JLabel mLabelHeaderScore;

	public Toplist(GameWindow gameWindow) {
		super(gameWindow);

		mLabelHeaderRank = new JLabel("Helyezés");
		mLabelHeaderRank.setFont(mHeaderFont);
		mLabelHeaderRank.setHorizontalAlignment(JLabel.CENTER);

		mLabelHeaderName = new JLabel("Név");
		mLabelHeaderName.setFont(mHeaderFont);
		mLabelHeaderName.setHorizontalAlignment(JLabel.CENTER);

		mLabelHeaderScore = new JLabel("Pontszám");
		mLabelHeaderScore.setFont(mHeaderFont);
		mLabelHeaderScore.setHorizontalAlignment(JLabel.CENTER);

		update(mToplistTable);

		mToplistTable.setLayout(new GridLayout(11, 3));

		final BorderLayout borderLayout = new BorderLayout(50, 30);
		setLayout(borderLayout);

		add(getHeaderLabel("Toplista"), BorderLayout.NORTH);
		add(mToplistTable, BorderLayout.CENTER);
		add(getBackButton(PanelId.GAME_MODE_SELECTOR), BorderLayout.SOUTH);
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
			mToplistTable.add(labelRank);

			final JLabel labelName = new JLabel(toplistItem.getName());
			labelName.setHorizontalAlignment(JLabel.CENTER);
			labelName.setFont(mStandardFont);
			mToplistTable.add(labelName);

			final JLabel labelScore = new JLabel(toplistItem.getPoints() + "");
			labelScore.setHorizontalAlignment(JLabel.CENTER);
			labelScore.setFont(mStandardFont);
			mToplistTable.add(labelScore);
		}
		toplistTable.revalidate();
		repaint();
	}
}
