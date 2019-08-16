package MessageFactory;

import Gui.GameObjects.Player;
import Gui.Interfaces.UNOConstants;
import Gui.Panel.UNOCard;
import org.json.JSONObject;
import java.awt.*;
import java.util.Iterator;

public class MessageParser {
    private Player player;
    public String colorChanged;
    
    public MessageParser(Player player) {
    	this.player = player;
     }

    public void clientJoined(String message) {
        String joinedUser = new JSONObject(message).getString("client joined");
        player.addNotifications(joinedUser + " has joined the game.");
    }

    public void drawCard(String message) {
        String type = new JSONObject(message).getJSONObject("draw card").getJSONObject("card").getString("type");
        String color = new JSONObject(message).getJSONObject("draw card").getJSONObject("card").getString("color");

        Color cardColor = determineColor(color);
        UNOCard drewCard = new UNOCard(cardColor, type);
        
        player.addCard(drewCard);
        player.addNotifications("You drew a " + drewCard.toString());
    }

    public void playedCard(String message) {
        if (player.isMyTurn()) {
            String playedType = new JSONObject(message).getJSONObject("played card").getString("type");
            String playedColor = new JSONObject(message).getJSONObject("played card").getString("color");
            String playedUser = new JSONObject(message).getJSONObject("played card").getString("username");

            Color color = determineColor(playedColor);

            player.endTurn();
            player.removeCard();

            UNOCard playedCard = new UNOCard(color, playedType);

            player.addNotifications(playedUser + " has played " + playedCard.toString());
        }
    }

    public void calledUno(String message) {
        String calledUno = new JSONObject(message).getString("called uno");
        player.addNotifications(calledUno + "has called uno.");
    }

    public void gameStarted(String message) {
        String gameStarted = new JSONObject(message).getString("game started");
        player.addNotifications(gameStarted);
    }

    public void gameEnded(String message) {
        String gameEnded = new JSONObject(message).getString("game ended");
        player.addNotifications(gameEnded);
    }

    public void yourTurn(String message) {
        player.startTurn();
        System.out.println(player.isMyTurn());
        player.addNotifications("it's your turn");
    }

    public void drewCard(String message) {
        String drewCardUser = new JSONObject(message).getString("drew card");
        player.addNotifications(drewCardUser + " drew a card.");;
    }

    public void gameWillStart(String message) {
        String willStart = new JSONObject(message).getString("game will start in 5 seconds");
        player.addNotifications(willStart);
    }

    public void colorChanged(String message) {
        colorChanged = new JSONObject(message).getString("color changed");
        player.setTableCard( new UNOCard(determineColor(colorChanged), player.getTableCard().getType()));
        player.addNotifications("The color has changed to " + colorChanged);
    }

    public void playerSkipped(String message) {
        String userSkipped = new JSONObject(message).getString("player skipped");
        player.addNotifications("The user " + userSkipped + " was skipped.");
    }

    public void reversed(String message) {
        String reversed = new JSONObject(message).getString("reversed");
        player.addNotifications(reversed);
    }

    public void turnChanged (String message) {
        String turnChanged = new JSONObject(message).getString("turn changed");
        player.addNotifications("turn changed " + turnChanged);
    }

    public void winner(String message) {
        String winner = new JSONObject(message).getString("winner");
        
        if(!winner.equals(player.getName())) {
        	player.setOpponentCardCount(0);
        }
        
        player.addNotifications("winner is " + winner);
    }

    public void reshuffle(String message) {
        String reshuffle = new JSONObject(message).getString("reshuffle");
        player.addNotifications(reshuffle);
    }
    
    public void cardCount(String message) {
    	JSONObject users = new JSONObject(message).getJSONObject("card count");
    	
    	System.out.println(users.toString());
		Iterator<String> keys = users.keys();
		String user1 = keys.next();
		String user2 = "";
		
		while(keys.hasNext()) {
			user2 = keys.next();
			System.out.println(user2);
		}
		
		if (player.getName().equals(user1)) {
			int cardCount = users.getInt(user2);
			player.setOpponentCardCount(cardCount);
	    	player.addNotifications("the opponent card count is: " + cardCount);			
		}else{
			int cardCount = users.getInt(user1);
			player.setOpponentCardCount(cardCount);
	    	player.addNotifications("the opponent card count is: " + cardCount);
		}

    }
    
    public void topCard(String message) {
        String type = new JSONObject(message).getJSONObject("top card").getString("type");
        String color = new JSONObject(message).getJSONObject("top card").getString("color");

        Color cardColor = determineColor(color);
        UNOCard tableCard = new UNOCard(cardColor, type);
        
        
        player.setTableCard(tableCard);
        
        if (colorChanged != null || player.getTableCard().getColorString().equals("Black")) {
        	player.setTableCard( new UNOCard(determineColor(colorChanged), player.getTableCard().getType()));
        }
        
        player.addNotifications("Top card on table is " + tableCard.toString());
    }

    private Color determineColor(String color){
        switch(color){
            case "Green":
                return UNOConstants.GREEN;
            case "Red":
                return UNOConstants.RED;
            case "Blue":
                return UNOConstants.BLUE;
            case "Yellow":
                return UNOConstants.YELLOW;
            case "Black":
                return UNOConstants.BLACK;
            default:
                System.out.println("Error in switch! Beep boop, unhandled color: " + color);
                return UNOConstants.BLACK;
        }
    }

}