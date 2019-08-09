package java_uno;

import modules.Handler;
import view.UNOCard;
import cardModel.CardDeck;

import org.json.JSONObject;

import java.util.Objects;

public class MessageHandler extends Handler {
    private static final String MODULE = "JavaUno";
    private static final MessageHandler handler = new MessageHandler();
    public static MessageHandler getInstance() {
        return handler;
    }

    private MessageHandler() {
        super(null);
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
            if (null != message.optJSONObject("action")) {
                JSONObject action = message.optJSONObject("action");

                if (action.has("play card") && null != action.optJSONObject("play card")) {
                    UNOCard card = UNOCard.convertJson(action.getJSONObject("play card").getJSONObject("card"));
                    Game.playCard(card, username);
                }
                else if (action.has("call uno")) {
                    String unoUsername = action.optString("call uno");

                    if (Objects.equals(username, unoUsername)) {
                        Game.callUno(username);
                    }
                    else {
                        Game.callUnoOn(unoUsername, username);
                    }
                }
                else if (action.has("color change")) {
                    Game.changeColor(action.optString("color change"), username);
                }
            }
            else if (!Objects.equals("", message.optString("action").trim())) {
                String action = message.getString("action");

                switch (action) {
                    case "join":
                        Game.addPlayer(username);
                        notifyAll("client joined", username);

                        break;
                    case "draw card":
                        Game.drawCard(username);
                        Game.updateTurn();

                        break;
                    case "quit":
                        Game.removePlayer(username);
                        break;
                    default:
                        // Do nothing on server internal messages

                        break;
                }
            }
        } catch (IllegalArgumentException e) {
            sendError(e, username);
        }
    }

    public void notify(String key, Object value, String username) {
        JSONObject message = new JSONObject();

        JSONObject notification = new JSONObject();
        notification.put(key, value);

        message.put("notify", notification);
        sendToPlayer(message, username);
    }

    protected void notifyAll(String key, Object value) {
        Game.activePlayers().forEach(username -> notify(key, value, username));
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
        new Thread(MessageHandler.getInstance()).start();
    }
}
