package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.Storage;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.model.ToplistItem;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.awt.Font;
import java.lang.Object;
import javax.swing.JLabel;

/**
 * A toplista megjelenítését végző panel
 */
public class Toplist extends GamePanel {

	private static final long serialVersionUID = -8944015110835172602L;
	
	public Toplist(GameWindow gameWindow) {
		super(gameWindow);
		
		Container toplistTable = new Container();
		
		final ArrayList<ToplistItem> toplistItems = Storage.getInstance().getToplistItems();
		int i=1;
		
		toplistTable.add(new JLabel("Helyezés"));
		toplistTable.add(new JLabel("Név"));
		toplistTable.add(new JLabel("Pontszám"));
		
		for (ToplistItem toplistItem : toplistItems) {
	
			String name = toplistItem.getName();
			int points = toplistItem.getPoints();
			
			toplistTable.add(new JLabel(i++ + "."));
			toplistTable.add(new JLabel(name));
			toplistTable.add(new JLabel(points + " points"));
			
		}
		
		toplistTable.setLayout(new GridLayout(11,3));
		add(toplistTable);
		
		add(getBackButton(PanelId.GAME_MODE_SELECTOR));
		
	}

}


