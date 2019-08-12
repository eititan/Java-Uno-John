package Gui.Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import Client.ClientSocket;
import Gui.GameObjects.Player;
import Gui.Interfaces.GameConstants;
import MessageFactory.MessageSender;

public class PlayerPanel extends JPanel implements GameConstants {

	private Player player;
	private String name;
	private Box myLayout, controlPanel;
	private JLayeredPane cardHolder;
	private ClientSocket client;
	private JButton draw, sayUNO;
	private JLabel nameLbl;
	private MyButtonHandler handler;

	public PlayerPanel(Player newPlayer, ClientSocket myClient) {
		this.player = newPlayer;
		this.client = myClient;

		myLayout = Box.createHorizontalBox();
		cardHolder = new JLayeredPane();
		cardHolder.setPreferredSize(new Dimension(600, 200));

		setControlPanel();

		myLayout.add(cardHolder);
		myLayout.add(Box.createHorizontalStrut(40));
		myLayout.add(controlPanel);
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
		nameLbl = new JLabel(name);
		draw.setBackground(new Color(79, 129, 189));
		draw.setFont(new Font("Arial", Font.BOLD, 20));
		draw.setFocusable(false);
		sayUNO.setBackground(new Color(149, 55, 53));
		sayUNO.setFont(new Font("Arial", Font.BOLD, 20));
		sayUNO.setFocusable(false);
		nameLbl.setForeground(Color.WHITE);
		nameLbl.setFont(new Font("Arial", Font.BOLD, 15));
		controlPanel = Box.createVerticalBox();
		controlPanel.add(nameLbl);
		controlPanel.add(draw);
		controlPanel.add(Box.createVerticalStrut(15));
		controlPanel.add(sayUNO);
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
		MessageSender ups = new MessageSender();
		public void actionPerformed(ActionEvent e) {
			
			if(player.isMyTurn()){
				
				if(e.getSource()==draw) {
					client.sendAction(ups.drawCardJson());
				}
				else if(e.getSource()==sayUNO) {
					client.sendAction(ups.callUnoJson(player.username));
				}
			}
		}
	}
}