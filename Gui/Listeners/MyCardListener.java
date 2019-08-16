package Gui.Listeners;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Gui.Panel.UNOCard;
import MessageFactory.MessageSender;
import Client.ClientSocket;

public class MyCardListener extends MouseAdapter {
	
	UNOCard card;
	ClientSocket client;
	private MessageSender ups;
	private UNOCard playedCard;
	public MyCardListener() {
		
	}
	
	public void setClient(ClientSocket Client){
		this.client = Client;
		ups = new MessageSender();
	}
	
	public void mousePressed(MouseEvent e) {		
		playedCard = (UNOCard) e.getSource();
		client.sendAction(ups.playCardJson(playedCard.getType(), playedCard.getColorString()));
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
		card = (UNOCard) e.getSource();
		Point p = card.getLocation();
		p.y -=20;
		card.setLocation(p);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		card = (UNOCard) e.getSource();
		Point p = card.getLocation();
		p.y +=20;
		card.setLocation(p);
	}	

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}	

	public UNOCard getPlayedCard() {
		return playedCard;
	}
}
