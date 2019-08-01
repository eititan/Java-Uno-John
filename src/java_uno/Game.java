package java_uno;

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

        handler.notifyAll("turn changed", username);
        handler.notify("your turn", username, username);
    }
}
