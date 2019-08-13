package MessageFactory;

import Gui.GameObjects.Player;
import Gui.Interfaces.UNOConstants;
import Gui.Panel.UNOCard;
import org.json.JSONObject;

import java.awt.*;

public class MessageParser {
    private Player player;

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
        String playedType = new JSONObject(message).getJSONObject("played card").getString("type");
        String playedColor = new JSONObject(message).getJSONObject("played card").getString("color");
        String playedUser = new JSONObject(message).getJSONObject("played card").getString("username");

        Color color = determineColor(playedColor);
        UNOCard playedCard = new UNOCard(color, playedType);
        
        player.addNotifications(playedUser + " has played " + playedCard.toString());
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
        String yourTurn = new JSONObject(message).getString("your turn");
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
        String colorChanged = new JSONObject(message).getString("color changed");
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
        player.addNotifications("winner is " + winner);
    }

    public void reshuffle(String message) {
        String reshuffle = new JSONObject(message).getString("reshuffle");
        player.addNotifications(reshuffle);
    }
    
    public void cardCount(String message) {
//    	String cardCount = new JSONObject(message).getString("card count");
//    	player.addNotifications("the opponent card count is: " + cardCount);
    }

    public void topCard(String message) {
        String type = new JSONObject(message).getJSONObject("top card").getString("type");
        String color = new JSONObject(message).getJSONObject("top card").getString("color");

        Color cardColor = determineColor(color);
        UNOCard tableCard = new UNOCard(cardColor, type);
        player.setTableCard(tableCard);
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