package brikks;

import brikks.essentials.*;

public class BlocksTable {
    public static final byte HEIGHT = 6;
    public static final byte WIDTH = 4;

    private final Block[][] blocks;

    public BlocksTable(final Block[][] table) {
        if (table == null) {
            throw new NullPointerException("table is null");
        }
        if (table.length != BlocksTable.HEIGHT || table[0].length != BlocksTable.WIDTH) {
            throw new IllegalArgumentException("table has to be " + BlocksTable.WIDTH + " x " + BlocksTable.HEIGHT);
        }

        this.blocks = table;
    }

    public Block getBlock(final Position choice) {
        return this.blocks[choice.getY()][choice.getX()];
    }

    public Block[] getRowOfBlocks(final byte row) {
        return this.blocks[row];
    }

    public Block[][] getTable() {
        return this.blocks;
    }
}
