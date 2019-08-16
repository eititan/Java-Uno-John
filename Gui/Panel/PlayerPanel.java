package Gui.Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import Client.ClientSocket;
import Gui.GameObjects.Player;
import Gui.Interfaces.GameConstants;
import MessageFactory.MessageSender;

public class PlayerPanel extends JPanel implements GameConstants {

	private Player player;
	private Box myLayout, controlPanel;
	private JLayeredPane cardHolder;
	private ClientSocket client;
	private JButton draw, sayUNO, changeColor;
	private JLabel nameLbl;
	private MyButtonHandler handler;
	private JFrame jFrame;
	private MessageSender ups;
	
	public PlayerPanel(Player newPlayer, ClientSocket myClient, JFrame myFrame) {
		this.player = newPlayer;
		this.client = myClient;
		this.jFrame = myFrame;
		
		ups = new MessageSender();
		myLayout = Box.createHorizontalBox();
		myLayout.setBackground(new Color(39, 119, 200));
		cardHolder = new JLayeredPane();
		cardHolder.setPreferredSize(new Dimension(600, 200));

		setControlPanel();

		myLayout.add(controlPanel);		
		myLayout.add(Box.createHorizontalStrut(10));
		myLayout.add(cardHolder);
		
		add(myLayout);

		handler = new MyButtonHandler();
		draw.addActionListener(handler);
		sayUNO.addActionListener(handler);
	}

	public void setCards() {
		cardHolder.removeAll();

		// Origin point at the center
		Point origin = getPoint(cardHolder.getWidth(), player.getTotalCards());
		int offset = calculateOffset(cardHolder.getWidth(),
				player.getTotalCards());

		int i = 0;
		for (UNOCard card : player.getCards()) {
			card.setBounds(origin.x, origin.y, card.CARDSIZE.width, card.CARDSIZE.height);
			cardHolder.add(card, i++);
			cardHolder.moveToFront(card);
			origin.x += offset;
		}
		revalidate();
		repaint();
	}

	private void setControlPanel() {
		draw = new JButton("Draw");
		sayUNO = new JButton("Say UNO");
		changeColor = new JButton("Change Color");
		nameLbl = new JLabel(player.getName());
		draw.setBackground(new Color(255,255,255));
		draw.setFont(new Font("Arial", Font.BOLD, 20));
		draw.setFocusable(false);
		sayUNO.setBackground(new Color(255,255,255));
		sayUNO.setFont(new Font("Arial", Font.BOLD, 20));
		sayUNO.setFocusable(false);
		changeColor.setBackground(new Color(255,255,255));
		changeColor.setFont(new Font("Arial", Font.BOLD, 20));
		changeColor.setFocusable(false);
		nameLbl.setForeground(Color.WHITE);
		nameLbl.setFont(new Font("Arial", Font.BOLD, 15));
		controlPanel = Box.createVerticalBox();
		controlPanel.add(nameLbl);
		controlPanel.add(draw);
		controlPanel.add(Box.createVerticalStrut(15));
		controlPanel.add(sayUNO);
		controlPanel.add(Box.createVerticalStrut(15));
		controlPanel.add(changeColor);
		
		changeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(player.getNotifications().getLast().contains("Black")) {
					Object[] options = {"Red",
			                "Yellow",
			                "Blue",
							"Green"};
					int n = JOptionPane.showOptionDialog(jFrame,//parent container of JOptionPane
							"Please pick a color to change to.",
							"Played a Wild Card",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,//do not use a custom Icon
							options,//the titles of buttons
							options[0]);//default button title
					client.sendAction(ups.changeColorJson(options[n].toString()));
				}
		
			}
		});
		
	}
    
	private int calculateOffset(int width, int totalCards) {
		int offset = 50;
		if (totalCards <= 8) {
			return offset;
		} else {
			double o = (width - 100) / (totalCards - 1);
			return (int) o;
		}
	}

	private Point getPoint(int width, int totalCards) {
		Point p = new Point(0, 20);
		if (totalCards >= 8) {
			return p;
		} else {
			p.x = (width - calculateOffset(width, totalCards) * totalCards) / 2;
			return p;
		}
	}
	
	class MyButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			if(player.isMyTurn()){
				
				if(e.getSource()==draw) {
					client.sendAction(ups.drawCardJson());
					player.endTurn();
				}
				else if(e.getSource()==sayUNO) {
					client.sendAction(ups.callUnoJson(player.getName()));
				}
			}
		}
	}
}
