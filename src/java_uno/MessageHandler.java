package java_uno;

import modules.Handler;
import view.UNOCard;
import cardModel.CardDeck;

import org.json.JSONObject;

import java.util.Objects;

public class MessageHandler extends Handler {
    private static final String MODULE = "JavaUno";

    private MessageHandler() {
        this(null);
    }
    private MessageHandler(String port) {
        super(port);
    }

    @Override
    protected void handle(JSONObject message) {
        if (null == message.optString("module") || !MODULE.equals(message.getString("module"))) {
            return;
        }

        String username = message.getString("username");

        if (!message.has("action")) {
            sendError(new Exception("JavaUno messages must have an 'action'"), username);
        }

        try {
            if (!Objects.equals("", message.optString("action").trim())) {
                String action = message.getString("action");

                switch (action) {
                    case "join":
                        Game.addPlayer(username);
                        notifyAll("joined", username);

                        break;
                    case "quit":
                        Game.removePlayer(username);
                        break;
                    default:
                        // Do nothing on server internal messages

                        break;
                }
            }
            else if (null != message.optJSONObject("action")){
                System.out.println(message);
            }
        } catch (IllegalArgumentException e) {
            sendError(e, username);
        }

        CardDeck deck = new CardDeck();
        UNOCard card = deck.drawCard();
        System.out.println(card.getType() + " " + card.getValue());
    }

    private void notifyAll(String key, Object value) {
        JSONObject message = new JSONObject();
        message.put(key, value);

        Game.activePlayers().forEach(username -> sendToPlayer(message, username));
    }

    private void sendError(Exception e, String username) {
        JSONObject error = new JSONObject();
        error.put("type", "error");
        error.put("message", e.getMessage());

        sendToPlayer(error, username);
    }

    private void sendToPlayer(JSONObject message, String username) {
        netSend(message, username, MODULE);
    }

    public static void main(String[] args) {
        new Thread(new MessageHandler()).start();
    }
}
