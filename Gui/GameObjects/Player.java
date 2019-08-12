package Gui.GameObjects;

import java.util.LinkedList;

import Gui.Panel.UNOCard;

public class Player {

	public String username;
	public boolean isTurn = false;
	public boolean iCalledUno = false;
	public boolean playedCard = false;
	public boolean drewCard = false;
	private LinkedList<String> notifications;
	public LinkedList<UNOCard> cards;

	public Player() {
		this.notifications = new LinkedList<>();
		this.cards = new LinkedList<>();
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

	public void removeCard(UNOCard card) {
		cards.remove(card);
	}

	public int getTotalCards(){
		return cards.size();
	}

	public boolean isMyTurn() {
		return isTurn;
	}

	public void setIsTurn() {
		this.isTurn = true;
	}

	public void endTurn() {
		this.isTurn = false;
	}

}

