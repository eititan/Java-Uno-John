package Gui.Panel;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;
import javax.swing.Box;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import Gui.GameObjects.Player;
import Gui.Interfaces.GameConstants;
import Gui.Interfaces.UNOConstants;


public class OpponentPanel extends JPanel implements GameConstants {

	private Player player;
	private Box myLayout, controlPanel;
	private JLayeredPane cardHolder;

	
	public OpponentPanel(Player newPlayer) {
		this.player = newPlayer;
		
		myLayout = Box.createHorizontalBox();
		cardHolder = new JLayeredPane();
		cardHolder.setPreferredSize(new Dimension(600, 200));

		controlPanel = Box.createVerticalBox();

		myLayout.add(cardHolder);
		myLayout.add(Box.createHorizontalStrut(40));
		myLayout.add(controlPanel);
		add(myLayout);

	}


	public void setCards() {
		
		cardHolder.removeAll();

		// Origin point at the center
		Point origin = getPoint(cardHolder.getWidth(), player.getOpponentCardCount());
		int offset = calculateOffset(cardHolder.getWidth(), player.getOpponentCardCount());

		LinkedList<UNOCard> opponentCards = new LinkedList<>();
		
		for (int i = 0; i < player.getOpponentCardCount(); i++) {
			opponentCards.add(new UNOCard(UNOConstants.BLACK, "UNO"));
		}

		if (player.getOpponentCardCount() > 0) {
			int i = 0;
			for (UNOCard card : opponentCards) {
			card.setBounds(origin.x, origin.y, card.CARDSIZE.width, card.CARDSIZE.height);
			cardHolder.add(card, i++);
			cardHolder.moveToFront(card);
			origin.x += offset;
			}
		} else if (player.getOpponentCardCount() == 0) {
			cardHolder.removeAll();	
		}
		
		revalidate();
		repaint();		
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
}
