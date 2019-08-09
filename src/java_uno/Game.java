package java_uno;

import cardModel.ActionCard;
import cardModel.CardDeck;
import cardModel.WildCard;
import interfaces.UNOConstants;
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

    public static void addPlayer(String username) {
        if (null == game) {
            game = new Game();
        }
        else if (game.players.size() > 0 && 0 == game.players.get(0).handSize()){
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
    private Color currentColor;
    private int skips;
    private String direction = "clockwise";

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
                throw new IllegalArgumentException("Invalid card played, top value is " + topCard.getValue() + " and color is " + game.currentColor);
            }
        }

        Player player = game.players.get(game.currentPlayer);
        if (!player.hasCard(card)) {
            throw new IllegalArgumentException("No card " + card.toJSON() + " in " + username + "'s hand");
        }

        game.discard.add(player.playCard(card));

        if (card instanceof ActionCard) {
            if (Objects.equals(UNOConstants.DRAW2PLUS, card.getValue())) {
                int current = game.currentPlayer > game.players.size() - 2 ? 0 : game.currentPlayer + 1;
                Game.drawCard(game.players.get(current).getUsername());
                Game.drawCard(game.players.get(current).getUsername());

                game.skips++;
            }
            else if (Objects.equals(UNOConstants.REVERSE, card.getValue())) {
                synchronized (game.players) {
                    List<Player> temp = new ArrayList<>(game.players);

                    Collections.reverse(temp);
                    game.players.clear();

                    game.players.addAll(temp);
                    game.currentPlayer = game.players.size() - (game.currentPlayer + 1);
                }
                game.direction = Objects.equals("clockwise", game.direction) ? "counter-clockwise" : "clockwise";

                MessageHandler.getInstance().notifyAll("reversed", game.direction);
            }
            else if (Objects.equals(UNOConstants.SKIP, card.getValue())) {
                game.skips++;

                String nextUsername = game.players.get(game.currentPlayer > game.players.size() - 2 ? 0 : game.currentPlayer + 1).getUsername();
                MessageHandler.getInstance().notifyAll("player skipped", nextUsername);
            }
        }
        if (card instanceof WildCard && Objects.equals(UNOConstants.W_DRAW4PLUS, card.getValue())) {
            int current = game.currentPlayer > game.players.size() - 2 ? 0 : game.currentPlayer + 1;
            String drawUsername = game.players.get(current).getUsername();
            Game.drawCard(drawUsername);
            Game.drawCard(drawUsername);
            Game.drawCard(drawUsername);
            Game.drawCard(drawUsername);

            game.skips++;
        }

        game.currentColor = game.discard.peekLast().getColor();

        MessageHandler handler = MessageHandler.getInstance();
        JSONObject cardMessage = card.toJSON();
        cardMessage.put("username", username);

        handler.notifyAll("played card", cardMessage);

        if (!Objects.equals(Color.black, game.currentColor)) {
            if (!Objects.equals(card.getColor(), topCard.getColor())) {
                handler.notifyAll("color changed", card.getColor());
            }

            Game.updateTurn();
        }
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

    public static void changeColor(String color, String username) {
        if (null == game) {
            throw new IllegalStateException("No game started to change the color");
        }
        String currentUser = game.players.size() > game.currentPlayer ?
                game.players.get(game.currentPlayer).getUsername() : null;

        if (!Objects.equals(currentUser, username)) {
            throw new IllegalArgumentException("User " + username + " is not the current player (" + currentUser + ")");
        }
        if (!Objects.equals(Color.BLACK, game.discard.peekLast().getColor())) {
            throw new IllegalArgumentException("Can't select a color from a non-wild card");
        }

        Color newColor;
        switch (color) {
            case "Blue":
                newColor = UNOConstants.BLUE;
                break;
            case "Green":
                newColor = UNOConstants.GREEN;
                break;
            case "Red":
                newColor = UNOConstants.RED;
                break;
            case "Yellow":
                newColor = UNOConstants.YELLOW;
                break;
            default:
                throw new IllegalArgumentException("Invalid color " + color);
        }

        game.currentColor = newColor;

        MessageHandler.getInstance().notifyAll("color changed", game.currentColor.toString());
        Game.updateTurn();
    }

    protected static synchronized void updateTurn() {
        if (game.players.stream().anyMatch(p -> 0 == p.handSize())) {
            MessageHandler handler = MessageHandler.getInstance();

            handler.notifyAll("winner", game.players.stream().filter(p -> 0 == p.handSize()).findFirst().get().getUsername());
            handler.notifyAll("game ended", "Game has ended");

            game = null;
            return;
        }

        while (game.skips >= 0) {
            if (game.currentPlayer < game.players.size() - 1) {
                game.currentPlayer++;
            } else {
                game.currentPlayer = 0;
            }
            game.skips--;
        }
        game.skips = 0;

        String username = game.players.get(game.currentPlayer).getUsername();

        MessageHandler handler = MessageHandler.getInstance();
        handler.notifyAll("turn changed", username);
        handler.notifyAll("top card", game.discard.peekLast().toJSON());
        handler.notify("your turn", username, username);

        JSONObject userCounts = new JSONObject();

        game.players.forEach(player -> userCounts.put(player.getUsername(), player.handSize()));

        handler.notifyAll("card count", userCounts);
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
                while (player.handSize() < 7) {
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
