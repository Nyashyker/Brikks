package brikks.essentials;

import brikks.essentials.enums.*;

public class Block {
    public static byte LEN = 4;

    protected final Position[] shape;
    protected final Color color;


    public Block(final Position[/* Block.len - 1 */] shape, final Color color) {
        this.shape = shape;
        this.color = color;
    }

    public Block(final Block other) {
        this(other.getBlock(), other.getColor());
    }


    public Position[] getBlock() {
        return this.shape;
    }

    public Color getColor() {
        return this.color;
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
