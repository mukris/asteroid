package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.Storage;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.model.ToplistItem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * A toplista megjelenítését végző panel
 */
public class Toplist extends GamePanel {
	private static final long serialVersionUID = -2546252829715261063L;

	public Toplist(GameWindow gameWindow) {
		super(gameWindow);

		final Container toplistTable = new Container();

		final ArrayList<ToplistItem> toplistItems = Storage.getInstance().getToplistItems();
		Collections.reverse(toplistItems);

		final JLabel labelToplist = new JLabel("Toplista");
		final JLabel jl_1 = new JLabel("Helyezés");
		final JLabel jl_2 = new JLabel("Név");
		final JLabel jl_3 = new JLabel("Pontszám");

		labelToplist.setFont(mHeaderFont);
		labelToplist.setHorizontalAlignment(JLabel.CENTER);

		jl_1.setFont(mHeaderFont);
		jl_1.setHorizontalAlignment(JLabel.CENTER);

		jl_2.setFont(mHeaderFont);
		jl_2.setHorizontalAlignment(JLabel.CENTER);

		jl_3.setFont(mHeaderFont);
		jl_3.setHorizontalAlignment(JLabel.CENTER);

		toplistTable.add(jl_1);
		toplistTable.add(jl_2);
		toplistTable.add(jl_3);

		int i = 0;
		for (ToplistItem toplistItem : toplistItems) {
			final JLabel labelRank = new JLabel(++i + ".");
			labelRank.setHorizontalAlignment(JLabel.CENTER);
			labelRank.setFont(mStandardFont);
			toplistTable.add(labelRank);

			final JLabel labelName = new JLabel(toplistItem.getName());
			labelName.setHorizontalAlignment(JLabel.CENTER);
			labelName.setFont(mStandardFont);
			toplistTable.add(labelName);

			final JLabel labelScore = new JLabel(toplistItem.getPoints() + "");
			labelScore.setHorizontalAlignment(JLabel.CENTER);
			labelScore.setFont(mStandardFont);
			toplistTable.add(labelScore);
		}

		toplistTable.setLayout(new GridLayout(11, 3));

		final BorderLayout borderLayout = new BorderLayout(50, 30);
		setLayout(borderLayout);

		add(labelToplist, BorderLayout.NORTH);
		add(toplistTable, BorderLayout.CENTER);
		add(getBackButton(PanelId.GAME_MODE_SELECTOR), BorderLayout.SOUTH);
	}
}
