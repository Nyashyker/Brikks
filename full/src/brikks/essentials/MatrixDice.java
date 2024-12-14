package brikks.essentials;

public class MatrixDice {
    private final Dice x;
    private final Dice y;

    public MatrixDice(final byte sidesX, final byte sidesY) {
        this.x = new Dice(sidesX);
        this.y = new Dice(sidesY);
    }

    public Position get() {
        return new Position(x.get(), y.get());
    }

    public Position roll() {
        return new Position(x.roll(), y.roll());
    }

    public void cheat(final Position value) {
        this.x.cheat(value.getX());
        this.y.cheat(value.getY());
    }
}
