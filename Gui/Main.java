package Gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Gui.GameObjects.Player;
import Gui.Panel.NotificationPanel;
import Gui.Panel.PlayerPanel;
import Gui.Panel.TablePanel;
import Gui.Panel.UNOCard;
import org.json.JSONObject;

import Client.ClientSocket;

import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Main extends JFrame {

	private JPanel contentPane;
	private static ClientSocket client;
	private static Player player;
	private static String username;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					username = JOptionPane.showInputDialog(null,"Please input username", "bob");
					player = new Player(username);
					client = new ClientSocket("127.0.0.1", 9886, player);
					Main frame = new Main(player, client);
					frame.setVisible(true);
					new Thread(client).start();
					
					ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
					exec.scheduleAtFixedRate(new Runnable() {
						@Override
						public void run() {
							NotificationPanel.txtNotification.setText("");

							for (String string : player.getNotifications()) {
								NotificationPanel.txtNotification.append(string + "\n");
							}
						}
					}, 0, 3, TimeUnit.SECONDS);
					

				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main(Player player, ClientSocket myClient) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 100, 450, 300);
		pack();
		setSize(1080,600);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(850, 600));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		PlayerPanel player1Panel = new PlayerPanel(player, myClient);
		contentPane.add(player1Panel, BorderLayout.SOUTH);

		//continuously refreshes cards in users hands
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				player1Panel.setCards();
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);

		PlayerPanel Player2Panel = new PlayerPanel(new Player(), myClient);
		contentPane.add(Player2Panel, BorderLayout.NORTH);
		
		setUpNotificationPanel();
		setupGameBoardPanel();
		
	}

	private void setUpNotificationPanel() {
		NotificationPanel NotificationPanel = new NotificationPanel(player);
		contentPane.add(NotificationPanel, BorderLayout.EAST);
	}

	private void setupGameBoardPanel() {
		JPanel GameInfoPanel = new JPanel();
		contentPane.add(GameInfoPanel, BorderLayout.CENTER);
		TablePanel GamePlayPanel = new TablePanel();
		GameInfoPanel.setLayout(new BorderLayout(0, 0));
		GameInfoPanel.add(GamePlayPanel, BorderLayout.CENTER);
		GamePlayPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel ActionButtons = new JPanel();
		GamePlayPanel.add(ActionButtons);
		ActionButtons.setLayout(new BorderLayout(0, 0));
		
		JPanel Buttons = new JPanel();
		ActionButtons.add(Buttons, BorderLayout.WEST);
		Buttons.setLayout(new GridLayout(4, 0, 0, 0));
		
		JButton btnPlaycard = new JButton("PlayCard");
		btnPlaycard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String indexStr = JOptionPane.showInputDialog("please pick an index");
				int index = Integer.parseInt(indexStr);	
				
				UNOCard myCard = player.getCard(index);
				
				JSONObject playCard = client.messageMan.playCardJson(myCard.getType(), myCard.getColorString());
				System.out.println(playCard.toString());
				client.sendAction(playCard);
				player.cards.remove(player.getCard(index));
			}
		});
		Buttons.add(btnPlaycard);
		
//		JButton btnDrawcard = new JButton("DrawCard");
//		btnDrawcard.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				JSONObject drawCard = client.messageMan.drawCardJson();
//				System.out.println(drawCard.toString());
//				client.sendAction(drawCard);
//			}
//		});
//		Buttons.add(btnDrawcard);
		
//		JButton btnCalluno = new JButton("CallUno");
//		btnCalluno.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JSONObject callUno = client.messageMan.callUnoJson(username);
//				System.out.println(callUno.toString());
//				client.sendAction(callUno);
//			}
//		});
//		Buttons.add(btnCalluno);
		
		JButton btnColorchange = new JButton("ColorChange");
		btnColorchange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String color = JOptionPane.showInputDialog("please pick a color in proper case (Red, Blue, Green, or Yellow)");
				JSONObject changeColor = client.messageMan.changeColorJson(color);
				System.out.println(changeColor.toString());
				client.sendAction(changeColor);			
			}
		});
		Buttons.add(btnColorchange);
		
		TablePanel GameBoardPanel = new TablePanel();
		ActionButtons.add(GameBoardPanel, BorderLayout.CENTER);
		
		JButton btnClickForCards = new JButton("click for cards");
		btnClickForCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < player.cards.size(); i++) {
					System.out.println(i + " " + player.cards.get(i));
				}
				
			}
		});
		GridBagConstraints gbc_btnClickForCards = new GridBagConstraints();
		gbc_btnClickForCards.insets = new Insets(0, 0, 0, 5);
		gbc_btnClickForCards.gridx = 0;
		gbc_btnClickForCards.gridy = 2;
		GameBoardPanel.add(btnClickForCards, gbc_btnClickForCards);
	}

}
