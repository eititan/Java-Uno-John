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

        if (game.players.stream().anyMatch(p -> Objects.equals(username, p.getUsername()))) {
            throw new IllegalArgumentException("Player " + username + " is already in the game");
        }

        game.players.add(new Player(username));
    }

    public static void removePlayer(String username) {
        if (null == game) {
            return;
        }

        Iterator<Player> iter = game.players.iterator();
        while (iter.hasNext()) {
            if (Objects.equals(username, iter.next().getUsername())) {
                iter.remove();

                break;
            }
        }

        if (0 == game.players.size()) {
            game = null;
        }
    }

    public static List<String> activePlayers() {
        return game.players.stream().map(Player::getUsername).collect(Collectors.toList());
    }

    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private AtomicLong timerStart;

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
        MessageHandler.getInstance().notifyAll("game will started", "Game will start");
    }
}
