package java_uno;

import cardModel.CardDeck;
import org.json.JSONObject;
import view.UNOCard;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Game {
    private static final long WAIT_LENGTH = 4000;
    private static Game game;
    private Color currentColor;

    public static void addPlayer(String username) {
        if (null == game) {
            game = new Game();
        }
        else {
            game.startTimer();
        }

        synchronized (game.players) {
            if (game.players.stream().anyMatch(p -> Objects.equals(username, p.getUsername()))) {
                throw new IllegalArgumentException("Player " + username + " is already in the game");
            }
        }

        game.players.add(new Player(username));
    }

    public static void removePlayer(String username) {
        if (null == game) {
            return;
        }

        synchronized (game.players) {
            Iterator<Player> iter = game.players.iterator();
            while (iter.hasNext()) {
                if (Objects.equals(username, iter.next().getUsername())) {
                    iter.remove();

                    break;
                }
            }
        }

        if (0 == game.players.size()) {
            game = null;
        }
    }

    public static List<String> activePlayers() {
        if (null == game) {
            return new ArrayList<>();
        }

        synchronized (game.players) {
            return game.players.stream().map(Player::getUsername).collect(Collectors.toList());
        }
    }

    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private Thread timerThread;
    private int currentPlayer;
    private CardDeck deck = new CardDeck();
    private Deque<UNOCard> discard = new ConcurrentLinkedDeque<>();

    public Game() {
        startTimer();
    }

    public static void playCard(UNOCard card, String username) {
        if (null == game) {
            throw new IllegalStateException("No game started to play card");
        }

        if (game.currentPlayer >= game.players.size() || !Objects.equals(username, game.players.get(game.currentPlayer).getUsername())) {
            throw new IllegalArgumentException("It isn't currently player " + username + "'s turn");
        }

        UNOCard topCard = game.discard.peekLast();

        if (!Objects.equals(card.getValue(), "W") && !Objects.equals(card.getValue(), "4+")) {
            if (!Objects.equals(card.getValue(), topCard.getValue()) && !Objects.equals(card.getColor(), game.currentColor)) {
                throw new IllegalArgumentException("Invalid card played, top card is " + topCard.toJSON());
            }
        }

        Player player = game.players.get(game.currentPlayer);
        if (!player.hasCard(card)) {
            throw new IllegalArgumentException("No card " + card.toJSON() + " in " + username + "'s hand");
        }

        game.discard.add(player.playCard(card));
        game.currentColor = game.discard.peekLast().getColor();

        if (Color.black.equals(game.currentColor)) {
            return;
        }

        MessageHandler handler = MessageHandler.getInstance();
        if (!Objects.equals(card.getColor(), topCard.getColor())) {
            handler.notifyAll("color changed", card.getColor());
        }
        JSONObject cardMessage = card.toJSON();
        cardMessage.put("username", username);

        handler.notifyAll("played card", cardMessage);
        game.updateTurn();
    }

    public static void callUno(String username) {
        if (null == game) {
            throw new IllegalStateException("No game started to call UNO");
        }

        MessageHandler handler = MessageHandler.getInstance();

        if (game.players.stream().anyMatch(p -> Objects.equals(username, p.getUsername()) && 1 == p.handSize())) {
            handler.notifyAll("called uno", username);
        }
    }

    public static void callUnoOn(String unoUsername, String username) {
        if (null == game) {
            throw new IllegalStateException("No game started to call UNO");
        }

        MessageHandler handler = MessageHandler.getInstance();

        if (game.players.stream().anyMatch(p -> Objects.equals(unoUsername, p.getUsername()) && 1 == p.handSize())) {
            handler.notifyAll("called uno", username);

            drawCard(unoUsername);
            drawCard(unoUsername);
        }
        else {
            throw new IllegalArgumentException(unoUsername + " doesn't have UNO");
        }
    }

    private synchronized void updateTurn() {
        if (game.players.stream().anyMatch(p -> 0 == p.handSize())) {
            MessageHandler handler = MessageHandler.getInstance();

            handler.notifyAll("winner", game.players.stream().filter(p -> 0 == p.handSize()).findFirst().get().getUsername());
            handler.notifyAll("game ended", "Game has ended");

            game = null;
            return;
        }

        if (currentPlayer < players.size() - 1) {
            currentPlayer++;
        }
        else {
            currentPlayer = 0;
        }

        String username = players.get(currentPlayer).getUsername();

        MessageHandler handler = MessageHandler.getInstance();
        handler.notifyAll("turn changed", username);
        handler.notifyAll("top card", discard.peekLast().toJSON());
        handler.notify("your turn", username, username);
    }

    private void startTimer() {
        if (null != timerThread) {
            timerThread.interrupt();
        }

        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(WAIT_LENGTH);

                    startGame();
                } catch (InterruptedException e) {
                    // Just stop this timer
                }
            }
        });

        timerThread.start();
    }

    private void startGame() {
        if (0 == players.size()) {
            game = null;
            return;
        }

        currentPlayer = 0;
        String username = game.players.get(currentPlayer).getUsername();

        MessageHandler handler = MessageHandler.getInstance();
        handler.notifyAll("game started", "Game has started");

        synchronized (game.players) {
            game.players.forEach(player -> {
                for (int i = 0; i < 7; i++) {
                    Game.drawCard(player.getUsername());
                }
            });
        }

        discard.add(deck.drawCard());
        while (Color.black.equals(discard.peekLast().getColor())) {
            discard.add(deck.drawCard());
        }

        currentColor = discard.peekLast().getColor();

        handler.notifyAll("top card", discard.peekLast().toJSON());
        handler.notifyAll("turn changed", username);
        handler.notify("your turn", username, username);
    }

    public static synchronized void drawCard(String username) {
        if (null == game) {
            throw new IllegalStateException("No game is currently in progress");
        }

        Player player = game.players.stream()
                .filter(p -> Objects.equals(username, p.getUsername()))
                .findFirst()
                .orElse(null);
        if (null == player) {
            throw new IllegalArgumentException("No player with username " + username + " is playing");
        }

        if (game.deck.isEmpty()) {
            UNOCard topCard = game.discard.removeLast();

            game.deck.shuffleIn(game.discard);
            game.discard.clear();
            game.discard.add(topCard);

            MessageHandler.getInstance().notifyAll("reshuffle", "Dealer reshuffling");
        }

        UNOCard card = game.deck.drawCard();

        player.drawCard(card);
        MessageHandler.getInstance().notifyAll("drew card", username);

        JSONObject cardMessage = new JSONObject();
        cardMessage.put("card", card.toJSON());

        MessageHandler.getInstance().notify("draw card", cardMessage, username);
    }
}
