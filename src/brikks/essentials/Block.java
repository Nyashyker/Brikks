package brikks.essentials;

import brikks.essentials.enums.*;

import java.util.Arrays;

public class Block {
    public static final byte LEN = 4;

    protected final Position[] shape;
    protected final Color color;


    public Block(final Position[/* Block.len - 1 */] shape, final Color color) {
        this.shape = shape;
        this.color = color;
    }

    public Block(final Block other) {
        this(other.shape, other.color);
    }


    public Position[] getBlock() {
        return this.shape;
    }

    public Position getSize() {
        final Position maxDistance = new Position();

        for (Position shape : this.shape) {
            if (shape.getX() < 0) {
                maxDistance.setX((byte) (maxDistance.getX() - shape.getX()));
            } else if (shape.getX() > maxDistance.getX()) {
                maxDistance.setX(shape.getX());
            }
            if (-shape.getY() > maxDistance.getY()) {
                maxDistance.setY((byte) -shape.getY());
            }
        }

        return maxDistance.add(new Position((byte) 1, (byte) 1));
    }

    public Color getColor() {
        return this.color;
    }


    public Block rotate() {
        return this.rotate(this.color);
    }

    public Block rotate(final Color color) {
        final Position[] shape = new Position[this.shape.length];

        final Position size = this.getSize();
        for (byte i = 0; i < shape.length; i++) {
            shape[i] = new Position((byte) -this.shape[i].getY(), (byte) (this.shape[i].getX() - (size.getX() - 1)));
        }
        Arrays.sort(shape, (final Position relativePos1, final Position relativePos2) -> {
            final byte y = (byte) (relativePos2.y - relativePos1.y);
            return y == 0 ? relativePos1.x - relativePos2.x : y;
        });

        return new Block(shape, color);
    }

    @Override
    public String toString() {
        StringBuilder shape = new StringBuilder();

        for (final Position p : this.shape) {
            shape.append(p.toString());
            shape.append(", ");
        }
        if (!shape.isEmpty()) {
            shape.delete(shape.length() - 2, shape.length());
        }

        return String.format("Shape{%s} - color=%s", shape, this.color);
    }
}
