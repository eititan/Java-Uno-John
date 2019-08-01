package java_uno;

import modules.Handler;
import view.UNOCard;
import cardModel.CardDeck;

import org.json.JSONObject;

public class MessageHandler extends Handler {
    private static final String MODULE = "JavaUno";
    private static MessageHandler handler;

    public static MessageHandler getInstance() {
        if (null == handler) {
            handler = new MessageHandler(null);
        }

        return handler;
    }

    private MessageHandler(String port) {
        super(port);
    }

    @Override
    protected void handle(JSONObject message) {
        System.out.println(message);
        if (null == message.optString("module") || !MODULE.equals(message.getString("module"))) {
            return;
        }

        String username = message.getString("username");

        if (!message.has("action")) {
            JSONObject error = new JSONObject();
            error.put("type", "error");
            error.put("message", "JavaUno messages must have an 'action'");

            sendToPlayer(error, username);
        }

        CardDeck deck = new CardDeck();
        UNOCard card = deck.drawCard();
        System.out.println(card.getType() + " " + card.getValue());
    }

    public void sendToPlayer(JSONObject message, String username) {
        netSend(message, username, MODULE);
    }

    public static void main(String[] args) {
        new Thread(MessageHandler.getInstance()).start();
    }
}
