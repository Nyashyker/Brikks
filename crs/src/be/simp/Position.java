package be.simp;

public class Position {
    private byte x;
    private byte y;


    public Position(final byte x, final byte y) {
        this.x = x;
        this.y = y;
    }

    public Position(final int x, final int y) {
        this((byte) x, (byte) y);
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

    public void setX(final int x) {
        this.setX((byte) x);
    }

    public void setY(final int y) {
        this.setY((byte) y);
    }

    public void set(final Position other) {
        this.x = other.getX();
        this.y = other.getY();
    }
}
