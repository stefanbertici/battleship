package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        Scanner scanner = new Scanner(System.in);
        Game game = new Game(player1, player2);

        game.play(scanner);
    }
}