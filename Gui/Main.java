package Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Gui.GameObjects.Player;
import Gui.Listeners.MyCardListener;
import Gui.Panel.NotificationPanel;
import Gui.Panel.OpponentPanel;
import Gui.Panel.PlayerPanel;
import Gui.Panel.TablePanel;
import Client.ClientSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main extends JFrame {

	private JPanel contentPane;
	private static ClientSocket client;
	private static Player player;
	private static String username;
	private static PlayerPanel playerPanel;
	private JFrame jFrame;
	private TablePanel gameBoardPanel;
	private OpponentPanel opponentPlayer;
	private static MyCardListener cardListener = new MyCardListener();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					username = JOptionPane.showInputDialog(null,"Please input username", "John");
					player = new Player(username);
					client = new ClientSocket("127.0.0.1", 9886, player);

					cardListener.setClient(client);
					player.setCardListener(cardListener);
					
					Main frame = new Main(player, client);
					frame.setVisible(true);
					frame.setTitle("Java Sucks");
					new Thread(client).start();
					
					frame.pack();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main(Player player, ClientSocket myClient) {
		setUpContentPane();
		
		playerPanel = new PlayerPanel(player, myClient, jFrame);
		playerPanel.setBackground(new Color(222,184,135));
		contentPane.add(playerPanel, BorderLayout.SOUTH);
		
		opponentPlayer = new OpponentPanel(player);
		opponentPlayer.setBackground(new Color(222,184,135));
		contentPane.add(opponentPlayer, BorderLayout.NORTH);
		
		NotificationPanel notificationPanel = new NotificationPanel(player);
		notificationPanel.txtNotification.setBackground(Color.LIGHT_GRAY);
		notificationPanel.setBackground(new Color(222,184,135));
		contentPane.add(notificationPanel, BorderLayout.WEST);
		
		setupGameBoardPanel();
		
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				
				playerPanel.setCards();
				
				NotificationPanel.txtNotification.setText("");
				for (String string : player.getNotifications()) {
					NotificationPanel.txtNotification.append(string + "\n");
				}
				NotificationPanel.txtNotification.setCaretPosition(NotificationPanel.txtNotification.getDocument().getLength());
				
				gameBoardPanel.setPlayedCard(player.getTableCard());
				
				opponentPlayer.setCards();	
			}
		}, 7000, 1500, TimeUnit.MILLISECONDS);
	}

	private void setUpContentPane() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 100, 450, 300);
		pack();
		setSize(1045,847);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(850, 800));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
	}

	private void setupGameBoardPanel() {
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel tablePlaceHolder = new JPanel();
		tablePlaceHolder.setLayout(new BorderLayout(0, 0));
		
		gameBoardPanel = new TablePanel();
		
		boardPanel.add(tablePlaceHolder, BorderLayout.CENTER);
		contentPane.add(boardPanel, BorderLayout.CENTER);
		
		JPanel board = new JPanel();
		board.setBackground(new Color(222,184,135));
		boardPanel.add(board);
		board.setLayout(new BorderLayout(0, 0));
		board.add(gameBoardPanel, BorderLayout.CENTER);
	}
}
