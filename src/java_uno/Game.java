package java_uno;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Game {
    private static Game game;
    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());

    public static void addPlayer(String username) {
        if (null == game) {
            game = new Game();
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
}
