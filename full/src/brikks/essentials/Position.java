package brikks.essentials;

public class Position {
    protected byte x;
    protected byte y;


    public Position() {
        this((byte) 0, (byte) 0);
    }

    public Position(final byte x, final byte y) {
        this.setX(x);
        this.setY(y);
    }

    public Position(final Position other) {
        this(other.x, other.y);
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
        this.setX(other.getX());
        this.setY(other.getY());
    }


    public Position add(final Position other) {
        return new Position((byte) (this.getX() + other.getX()), (byte) (this.getY() + other.getY()));
    }

    public boolean equals(final Position other) {
        return this.getX() == other.getX() && this.getY() == other.getY();
    }


    @Override
    public String toString() {
        return String.format("Pos(%d, %d)", this.x, this.y);
    }
}
