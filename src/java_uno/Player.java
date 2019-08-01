package java_uno;

import view.UNOCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private String username;
    private List<UNOCard> hand = Collections.synchronizedList(new ArrayList<>());

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void drawCard(UNOCard drawCard) {
        hand.add(drawCard);
    }
}
