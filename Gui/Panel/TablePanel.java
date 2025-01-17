package Gui.Panel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import Gui.Interfaces.GameConstants;


public class TablePanel extends JPanel implements GameConstants {
	
	private UNOCard topCard = new UNOCard(Color.WHITE, "");
	private JPanel table;
	
	public TablePanel(){
		setOpaque(false);
		setLayout(new GridBagLayout());
		table = new JPanel();
		setTable();
		setComponents();
	}
	
	private void setTable(){
		removeAll();
		table.setSize(100,400);
		table.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		table.add(topCard, c);
		setComponents();
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
	}

	public void setPlayedCard(UNOCard playedCard){
		table.removeAll();
		topCard = playedCard;
		setTable();
	}
}
