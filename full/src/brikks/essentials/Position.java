package brikks.essentials;

public class Position {
    private byte x;
    private byte y;


    public Position(final byte x, final byte y) {
        this.x = x;
        this.y = y;
    }


    public byte getX() {
        return this.x;
    }

    public byte getY() {
        return this.y;
    }

    public void setX(final byte x) {
        this.x = x;
    }

    public void setY(final byte y) {
        this.y = y;
    }

    public void set(final Position other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    @Override
    public String toString() {
        return String.format("Pos(%d, %d)", this.x, this.y);
    }
}
