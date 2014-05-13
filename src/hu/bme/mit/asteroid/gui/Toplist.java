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
import java.awt.Color;
import java.awt.Font;

/**
 * A toplista megjelenítését végző panel
 */
public class Toplist extends GamePanel {

	private static final long serialVersionUID = -8944015110835172602L;
	
	public Toplist(GameWindow gameWindow) {
		super(gameWindow);
		
		Container toplistTable = new Container();
		
		final ArrayList<ToplistItem> toplistItems = Storage.getInstance().getToplistItems();
		int i=10;
		
		JLabel jl_1 = new JLabel("Helyezés");
		JLabel jl_2 = new JLabel("Név");
		JLabel jl_3 = new JLabel("Pontszám");
		
		jl_1.setForeground(new Color(0,0,200));
		jl_1.setFont(new Font("Serif",1,20));
		
		jl_2.setForeground(new Color(0,200,0));
		jl_2.setFont(new Font("Serif",1,20));
		
		jl_3.setForeground(new Color(200,0,0));
		jl_3.setFont(new Font("Serif",1,20));
		
		toplistTable.add(jl_1);
		toplistTable.add(jl_2);
		toplistTable.add(jl_3);
		
		for (ToplistItem toplistItem : toplistItems) {
	
			String name = toplistItem.getName();
			int points = toplistItem.getPoints();
			
			toplistTable.add(new JLabel(i-- + "."));
			toplistTable.add(new JLabel(name));
			toplistTable.add(new JLabel(points + " pont"));
			
		}
		
		toplistTable.setLayout(new GridLayout(11,3));
		add(toplistTable);
		
		add(getBackButton(PanelId.GAME_MODE_SELECTOR));
		
	}

}


