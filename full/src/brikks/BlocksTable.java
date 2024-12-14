package brikks;

import brikks.essentials.Block;

public class BlocksTable {
    public final static byte height = 6;
    public final static byte width = 4;

    private final Block[][] blocks;

    public BlocksTable(final Block[][] table) {
        this.blocks = table;
    }

    public Block getBlock(final byte column, final byte row) {
        return this.blocks[row][column];
    }

    public Block getBlock(final int column, final int row) {
        return this.getBlock((byte) column, (byte) row);
    }

    public Block[] getRowOfBlocks(final byte row) {
        return this.blocks[row];
    }

    public Block[] getRowOfBlocks(final int row) {
        return this.getRowOfBlocks((byte) row);
    }

    public Block[][] getTable() {
        return this.blocks;
    }
}
