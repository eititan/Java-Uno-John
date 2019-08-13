package Gui.Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;

import Gui.GameObjects.Player;
import Gui.Interfaces.GameConstants;
import Gui.Interfaces.UNOConstants;

public class TablePanel extends JPanel implements GameConstants {
	
	private UNOCard topCard = new UNOCard(UNOConstants.RED, "6");
	private JPanel table;
	private Player player;
	
	//public TablePanel(UNOCard firstCard){
	public TablePanel(Player player){
		this.player = player;

		setOpaque(false);
		setLayout(new GridBagLayout());

		//topCard = player.getTableCard();
		table = new JPanel();
		table.setBackground(new Color(64,64,64));
		setTable();
		setComponents();
	}
	
	private void setTable(){
		
		table.setPreferredSize(new Dimension(300, 150));
		table.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		table.add(topCard, c);
	}
	
	private void setComponents() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 130, 0, 45);
		add(table,c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 1, 0, 1);
		add(infoPanel, c);	
	}

	public void setPlayedCard(UNOCard playedCard){
		table.removeAll();
		topCard = playedCard;
		setTable();
		setBackgroundColor(playedCard);
	}
	
	public void setBackgroundColor(UNOCard playedCard){
		Color background;
//		if(playedCard.getType()== "2+") {
//			background = c;
//		}
//		else {
		background = playedCard.getColor();
		System.out.println(player.getTableCard().toString());
		table.setBackground(background);
	}
}
