package battleship;

public class Ship {
    private String name;
    private int length;
    private int healthPoints;

    public Ship(String name, int length) {
        this.name = name;
        this.length = length;
        this.healthPoints = length;
    }

    public String getName() {
        return this.name;
    }

    public int getLength() {
        return this.length;
    }

    public void shoot() {
        this.healthPoints--;
        if (this.healthPoints < 0) {
            this.healthPoints = 0;
        }
    }

    public boolean isSunk() {
        return this.healthPoints <= 0;
    }
}
