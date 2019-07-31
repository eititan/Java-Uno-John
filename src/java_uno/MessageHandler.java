package java_uno;

import modules.Handler;
import view.UNOCard;
import cardModel.CardDeck;

import org.json.JSONObject;

public class MessageHandler extends Handler {
    private static final String MODULE = "JavaUno";

    public MessageHandler() {
        this(null);
    }
    public MessageHandler(String port) {
        super(port);
    }

    @Override
    protected void handle(JSONObject message) {
        System.out.println(message);
        if (null == message.optString("module") || !MODULE.equals(message.getString("module"))) {
            return;
        }
        CardDeck deck = new CardDeck();
        UNOCard card = deck.drawCard();
        System.out.println(card.getType() + " " + card.getValue());
    }

    public static void main(String[] args) {
        new Thread(new MessageHandler()).start();
    }
}
