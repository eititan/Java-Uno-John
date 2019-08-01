package java_uno;

import view.UNOCard;

import java.util.*;

public class Player {
    private String username;
    private final List<UNOCard> hand = Collections.synchronizedList(new ArrayList<>());

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void drawCard(UNOCard drawCard) {
        hand.add(drawCard);
    }

    public boolean hasCard(UNOCard card) {
        synchronized (hand) {
            return hand.stream().anyMatch(c -> Objects.equals(c, card));
        }
    }

    public UNOCard playCard(UNOCard card) {
        if (!hasCard(card)) {
            throw new IllegalArgumentException("No card " + card.toJSON() + " in player " + username);
        }

        UNOCard toPlay = null;

        synchronized (hand) {
            Iterator<UNOCard> iter = hand.iterator();
            while (iter.hasNext()) {
                UNOCard next = iter.next();
                if (Objects.equals(next, card)) {
                    toPlay = next;
                    iter.remove();

                    break;
                }
            }
        }

        return toPlay;
    }

    public int handSize() {
        return hand.size();
    }
}
