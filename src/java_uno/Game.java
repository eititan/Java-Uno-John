package java_uno;

import cardModel.CardDeck;
import org.json.JSONObject;
import view.UNOCard;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Game {
    private static final long WAIT_LENGTH = 4000;
    private static Game game;

    public static void addPlayer(String username) {
        if (null == game) {
            game = new Game();
        }
        else {
            game.timerStart = new AtomicLong(System.currentTimeMillis());
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
    private AtomicLong timerStart;
    private int currentPlayer;
    private CardDeck deck = new CardDeck();

    public Game() {
        startTimer();
    }

    private void startTimer() {
        //noinspection Convert2Lambda
        new Thread(new Runnable() {
            @Override
            public void run() {
                timerStart = new AtomicLong(System.currentTimeMillis());

                long currentTimerStart = timerStart.longValue();
                while (System.currentTimeMillis() < timerStart.longValue() + WAIT_LENGTH) {
                    if (currentTimerStart != timerStart.longValue()) {
                        return;
                    }

                    Thread.onSpinWait();
                }

                startGame();
            }
        }).start();
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

        handler.notifyAll("turn changed", username);
        handler.notify("your turn", username, username);
    }

    private static synchronized void drawCard(String username) {
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

        UNOCard card = game.deck.drawCard();

        player.drawCard(card);
        MessageHandler.getInstance().notifyAll("drew card", username);

        JSONObject cardMessage = new JSONObject();
        cardMessage.put("card", card.toJSON());

        MessageHandler.getInstance().notify("draw card", cardMessage, username);
    }
}
