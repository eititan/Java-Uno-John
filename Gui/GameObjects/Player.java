package Gui.GameObjects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import Gui.Listeners.MyCardListener;
import Gui.Panel.UNOCard;

public class Player {

	private String username;
	private boolean isTurn = false;
	private	UNOCard tableCard;
	private LinkedList<String> notifications;
	public LinkedList<UNOCard> cards;
	private int opponentCardCount = 7;
	private boolean drewCard = false;
	private MyCardListener cardListener;
	public String recentColorChange;
	
	public Player() {		
		this.notifications = new LinkedList<>();
		this.cards = new LinkedList<>();
	}

	public void setCardListener(MyCardListener CardListener) {
		this.cardListener = CardListener;
	}
	
	public Player(String name) {
		setName(name);
		this.notifications = new LinkedList<>();
		this.cards = new LinkedList<>();
	}

	public void setName(String userName) {
		this.username = userName;
	}

	public String getName() {
		return this.username;
	}

	public void addCard(UNOCard card) {
		addCardListener(cardListener, card);
		cards.add(card);
	}

	public UNOCard getCard(int index) {
		return cards.get(index);
	}

	public void addNotifications(String message) {
		notifications.add(message);
		System.out.println(message);
	}

	public LinkedList<String> getNotifications() {
		return notifications;
	}

	public LinkedList<UNOCard> getCards() {
		return cards;
	}

	public void removeCard() {
		UNOCard playedCard = cardListener.getPlayedCard();
		cards.remove(playedCard);
	}

	public int getTotalCards(){
		return cards.size();
	}

	public boolean isMyTurn() {
		return isTurn;
	}

	public void startTurn() {
		this.isTurn = true;
	}

	public void endTurn() {
		this.isTurn = false;
	}

	public void setTableCard(UNOCard card){
		System.out.println(card.toString());
		this.tableCard = card;
	}

	public UNOCard getTableCard(){
		return tableCard;
	}
	
	public void setOpponentCardCount(int oppCardCount) {
		this.opponentCardCount = oppCardCount;
	}
	
	public int getOpponentCardCount() {
		return opponentCardCount;
	}

	public void setDrewCard() {
		this.drewCard = true;
	}
	
	public boolean getDrewCard() {
		return drewCard;
	}
	
	private void addCardListener(MyCardListener listener, UNOCard card){
		card.addMouseListener(listener);
	}
}

