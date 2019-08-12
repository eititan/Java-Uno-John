package Gui.Panel;

import Gui.GameObjects.Player;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


import java.awt.BorderLayout;

public class NotificationPanel extends JPanel {

	public static JTextArea txtNotification;
	
	public NotificationPanel(Player player) {
		setLayout(new BorderLayout(0, 0));
		
		JLabel NotifyLabel = new JLabel("Notifications");
		NotifyLabel.setHorizontalAlignment(JLabel.CENTER);
		add(NotifyLabel, BorderLayout.NORTH);
		
		txtNotification = new JTextArea();
		JScrollPane ScrollPane = new JScrollPane(txtNotification, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		txtNotification.setPreferredSize(new Dimension(250,200));
		txtNotification.setLineWrap(true);
		
		add(ScrollPane, BorderLayout.CENTER);
	}

}
