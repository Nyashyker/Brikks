package brikks.essentials;

public class Block {
    public static byte LEN = 4;

    protected final Position[] shape;
    protected final Color color;


    public Block(final Position[/* Block.len - 1 */] shape, final Color color) {
        this.shape = shape;
        this.color = color;
    }

    public Block(final Block other) {
        this.shape = other.getBlock();
        this.color = other.getColor();
    }


    public Position[] getBlock() {
        return this.shape;
    }

    public Color getColor() {
        return this.color;
    }
}
