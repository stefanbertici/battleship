package battleship;

public class Player {
    private String name;
    private int floatingShips;
    private final char[][] field;
    private final char[][] fieldWithFog;
    private final Ship[][] fieldWithShips;

    public Player(String name) {
        this.name = name;
        this.floatingShips = 5;
        this.field = new char[10][10];
        this.fieldWithFog = new char[10][10];
        this.fieldWithShips = new Ship[10][10];
    }

    public String getName() {
        return this.name;
    }

    public void sinkShip() {
        if (this.floatingShips > 0) {
            this.floatingShips--;
        }
    }

    public int getFloatingShips() {
        return this.floatingShips;
    }

    public boolean isDefeated() {
        return this.floatingShips == 0;
    }

    public void initializeFields() {
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[0].length; col++) {
                field[row][col] = '~';
                fieldWithFog[row][col] = '~';
                fieldWithShips[row][col] = null;
            }
        }
    }

    public void drawField(boolean withFog) {
        char rowCharacter = 'A';
        char[][] fieldToPrint;

        if (withFog) {
            fieldToPrint = fieldWithFog;
        } else {
            fieldToPrint = field;
        }

        System.out.println("  1 2 3 4 5 6 7 8 9 10");

        for (int row = 0; row < fieldToPrint.length; row++) {
            System.out.print(rowCharacter + " ");
            for (int col = 0; col < fieldToPrint[0].length; col++) {
                System.out.print(fieldToPrint[row][col] + " ");
            }

            System.out.println();
            rowCharacter++;
        }
    }

    public char getFieldCell(int row, int col) {
        return field[row][col];
    }

    public void setFieldCell(int row, int col, char value) {
        field[row][col] = value;
    }

    public Ship getFieldWithShipsCell(int row, int col) {
        return fieldWithShips[row][col];
    }

    public void setFieldWithShipsCell(int row, int col, Ship ship) {
        fieldWithShips[row][col] = ship;
    }

    public void setFieldWithFogCell(int row, int col, char value) {
        fieldWithFog[row][col] = value;
    }
}
