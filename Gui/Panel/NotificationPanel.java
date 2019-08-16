package Gui.Panel;

import Gui.GameObjects.Player;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;

public class NotificationPanel extends JPanel {

	public static JTextArea txtNotification;
	
	public NotificationPanel(Player player) {
		setLayout(new BorderLayout(0, 0));

		JLabel NotifyLabel = new JLabel("Notifications");
		NotifyLabel.setHorizontalAlignment(JLabel.CENTER);		
		NotifyLabel.setForeground(new Color(255,255,255));
		NotifyLabel.setFont(new Font("Arial", Font.BOLD, 20));
		NotifyLabel.setFocusable(false);
		
		
		add(NotifyLabel, BorderLayout.NORTH);
		
		txtNotification = new JTextArea();
		txtNotification.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(txtNotification, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum());
		scrollBar.setSize(new Dimension(250,200));
		txtNotification.setCaretPosition(txtNotification.getDocument().getLength());
		txtNotification.setColumns(20);
		add(scrollPane, BorderLayout.CENTER);
	}

}
