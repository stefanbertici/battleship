package battleship;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    private Player player1;
    private Player player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void initializeFields() {
        player1.initializeFields();
        player2.initializeFields();
    }

    public void drawField(Player player, boolean withFog) {
        player.drawField(withFog);
    }

    public int[] convert(String begin, String end) {
        int[] coordinates = new int[4];

        //converting beginning coordinates into integer rows and cols [0-9]
        coordinates[0] = begin.charAt(0) - 65;
        if (begin.length() == 2) {
            coordinates[1] = begin.charAt(1) - 49;
        } else if (begin.length() == 3 && begin.charAt(1) == '1' && begin.charAt(2) == '0'){
            coordinates[1] = 9;
        } else {
            coordinates[1] = -1;
        }

        //converting end coordinates into integer rows and cols [0-9]
        coordinates[2] = end.charAt(0) - 65;
        if (end.length() == 2) {
            coordinates[3] = end.charAt(1) - 49;
        } else if (end.length() == 3 && end.charAt(1) == '1' && end.charAt(2) == '0'){
            coordinates[3] = 9;
        }else {
            coordinates[3] = -1;
        }

        //we sort the coordinates so begin is always smaller than end
        int aux;
        if (coordinates[0] > coordinates[2]) {
            aux = coordinates[0];
            coordinates[0] = coordinates[2];
            coordinates[2] = aux;
        } else if (coordinates[1] > coordinates[3]) {
            aux = coordinates[1];
            coordinates[1] = coordinates[3];
            coordinates[3] = aux;
        }

        return coordinates;
    }

    public int[] convert(String coord) {
        int[] coordinates = new int[2];

        //converting coordinate into integer rows and cols [0-9]
        coordinates[0] = coord.charAt(0) - 65;
        if (coord.length() == 2) {
            coordinates[1] = coord.charAt(1) - 49;
        } else if (coord.length() == 3 && coord.charAt(1) == '1' && coord.charAt(2) == '0'){
            coordinates[1] = 9;
        } else {
            coordinates[1] = -1;
        }

        return coordinates;
    }

    public boolean isValidToPlace(Player player, Ship ship, String begin, String end) {
        int[] coordinates = convert(begin, end);
        int beginRow = coordinates[0];
        int beginCol = coordinates[1];
        int endRow = coordinates[2];
        int endCol = coordinates[3];

        if (beginRow < 0 || beginRow > 9 || beginCol < 0 || beginCol > 9 || endRow < 0 || endRow > 9 || endCol < 0 || endCol > 9) {
            System.out.println("\nError! Coordinates out of bounds! Try again:");
            return false;
        }

        if (beginRow != endRow && beginCol != endCol) {
            System.out.println("\nError! Wrong ship location! Try again:");
            return false;
        }

        if (beginRow == endRow) {
            if (Math.abs(endCol - beginCol) != ship.getLength() - 1) {
                System.out.println("\nError! Wrong length of the " + ship.getName() + "! Try again:");
                return false;
            } else {
                for (int i = beginCol; i <= endCol; i++) {
                    if (beginRow > 0) {
                        if (player.getFieldCell(beginRow - 1, i) == 'O') {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                            return false;
                        }
                    }

                    if (beginRow < 9) {
                        if (player.getFieldCell(beginRow + 1, i) == 'O') {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                            return false;
                        }
                    }

                    if (player.getFieldCell(beginRow, i) == 'O') {
                        System.out.println("\nError! You placed it too close to another one. Try again:");
                        return false;
                    }

                    if (beginCol > 0) {
                        if (player.getFieldCell(beginRow, beginCol - 1) == 'O') {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                            return false;
                        }
                    }

                    if (endCol < 9) {
                        if (player.getFieldCell(beginRow, endCol + 1) == 'O') {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                            return false;
                        }
                    }
                }
            }
        } else if (beginCol == endCol) {
            if (Math.abs(endRow - beginRow) != ship.getLength() - 1) {
                System.out.println("\nError! Wrong length of the " + ship.getName() + "! Try again:");
                return false;
            } else {
                for (int i = beginRow; i <= endRow; i++) {
                    if (beginCol > 0) {
                        if (player.getFieldCell(i, beginCol - 1) == 'O') {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                            return false;
                        }
                    }

                    if (beginCol < 9) {
                        if (player.getFieldCell(i, beginCol + 1) == 'O') {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                            return false;
                        }
                    }

                    if (player.getFieldCell(i, beginCol) == 'O') {
                        System.out.println("\nError! You placed it too close to another one. Try again:");
                        return false;
                    }

                    if (beginRow > 0) {
                        if (player.getFieldCell(beginRow - 1, beginCol) == 'O') {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                            return false;
                        }
                    }

                    if (endRow < 9) {
                        if (player.getFieldCell(endRow + 1, beginCol) == 'O') {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public void placeShip(Player player, Ship ship, Scanner scanner) {
        boolean withFog = true;
        System.out.printf("\nEnter the coordinates of the %s (%d cells):\n", ship.getName(), ship.getLength());
        while (true) {
            String input;
            String[] parts = {"",""};

            //we make sure that the user input is in the expected format
            while(true) {
                System.out.print("\n> ");
                input = scanner.nextLine();
                Pattern pattern = Pattern.compile("[A-J]([1-9]|10)\\s[A-J]([1-9]|10)");
                Matcher matcher = pattern.matcher(input);
                if (matcher.matches()) {
                    parts = input.split("\\s");
                    break;
                } else {
                    System.out.println("\nWrong coordinate format. Try again:");
                }
            }

            if (isValidToPlace(player, ship, parts[0], parts[1])) {
                //converting beginning coordinates from String to integer values [0-9]
                int[] coordinates = convert(parts[0], parts[1]);
                int beginRow = coordinates[0];
                int beginCol = coordinates[1];
                int endRow = coordinates[2];
                int endCol = coordinates[3];

                //place the ship onto the field
                if (beginRow == endRow) {
                    for (int i = beginCol; i <= endCol; i++) {
                        player.setFieldCell(beginRow, i, 'O');
                        player.setFieldWithShipsCell(beginRow, i, ship);
                    }
                } else if (beginCol == endCol) {
                    for (int i = beginRow; i <= endRow; i++) {
                        player.setFieldCell(i, beginCol, 'O');
                        player.setFieldWithShipsCell(i, beginCol, ship);
                    }
                }

                System.out.println();
                player.drawField(!withFog);
                break;
            }
        }
    }

    public void play(Scanner scanner) {
        //initializing the ships for both players, ships[0-4] for p1 and ships[5-9] for p2
        Ship[] ships = new Ship[10];
        ships[0] = new Ship("Aircraft Carrier", 5);
        ships[1] = new Ship("Battleship", 4);
        ships[2] = new Ship("Submarine", 3);
        ships[3] = new Ship("Cruiser", 3);
        ships[4] = new Ship("Destroyer", 2);
        ships[5] = new Ship("Aircraft Carrier", 5);
        ships[6] = new Ship("Battleship", 4);
        ships[7] = new Ship("Submarine", 3);
        ships[8] = new Ship("Cruiser", 3);
        ships[9] = new Ship("Destroyer", 2);

        boolean withFog = true;

        initializeFields();
        System.out.println();
        drawField(player1, withFog);
        System.out.printf("\n%s, place your ships on the game field\n", player1.getName());
        for (int i = 0; i < ships.length / 2; i++) {
            placeShip(player1, ships[i], scanner);
        }

        while(true) {
            System.out.println("\nPress Enter and pass the move to another player");
            String input = scanner.nextLine();
            if (input.equals("")) {
                break;
            }
        }

        drawField(player2, withFog);
        System.out.printf("\n%s, place your ships on the game field\n", player2.getName());
        for (int i = 5; i < ships.length; i++) {
            placeShip(player2, ships[i], scanner);
        }

        while(true) {
            System.out.println("\nPress Enter and pass the move to another player");
            String input = scanner.nextLine();
            if (input.equals("")) {
                break;
            }
        }

        Player player, enemy;
        boolean playerOneTurn = true;
        //while game is not over, continue with game flow
        while (!player1.isDefeated() && !player2.isDefeated()) {
            if (playerOneTurn) {
                player = player1;
                enemy = player2;
            } else {
                player = player2;
                enemy = player1;
            }

            enemy.drawField(withFog);
            System.out.println("---------------------");
            player.drawField(!withFog);
            System.out.printf("\n%s, it's your turn:\n", player.getName());
            String input;
            //we make sure that the user input is in the expected format
            while(true) {
                System.out.print("\n> ");
                input = scanner.nextLine();
                Pattern pattern = Pattern.compile("[A-J]([1-9]|10)");
                Matcher matcher = pattern.matcher(input);
                if (matcher.matches()) {
                    break;
                } else {
                    System.out.println("\nWrong coordinate format. Try again:");
                }
            }

            int[] coordinates = convert(input);
            int row = coordinates[0];
            int col = coordinates[1];
            if (enemy.getFieldCell(row, col) == '~') {
                enemy.setFieldCell(row, col, 'M');
                enemy.setFieldWithFogCell(row, col, 'M');
                System.out.println("\nYou missed!");
            } else if (enemy.getFieldCell(row, col) == 'X' || enemy.getFieldCell(row, col) == 'M') {
                System.out.println("\nYou already hit that cell!");
            } else {
                enemy.setFieldCell(row, col, 'X');
                enemy.setFieldWithFogCell(row, col, 'X');
                enemy.getFieldWithShipsCell(row, col).hit();
                if (enemy.getFieldWithShipsCell(row, col).isSunk()) {
                    if (enemy.getFloatingShips() > 1) {
                        System.out.println("\nYou sank a ship!");
                    } else {
                        System.out.println("\nYou sank the last ship. You Won. Congratulations!");
                    }
                    enemy.sinkShip();
                } else {
                    System.out.println("\nYou hit a ship!");
                }
            }

            if (playerOneTurn) {
                playerOneTurn = false;
            } else {
                playerOneTurn = true;
            }

            while(true) {
                System.out.println("\nPress Enter and pass the move to another player\n");
                input = scanner.nextLine();
                if (input.equals("")) {
                    break;
                }
            }
        }
    }
}