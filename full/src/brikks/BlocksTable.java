package brikks;

import brikks.essentials.Block;

public class BlocksTable {
    public final static byte HEIGHT = 6;
    public final static byte WIDTH = 4;

    private final Block[][] blocks;

    public BlocksTable(final Block[][] table) {
        if (table == null) {
            throw new NullPointerException("table is null");
        }
        if (table.length == 0 || table[0].length == 0) {
            throw new IllegalArgumentException("table is empty");
        }

        this.blocks = table;
    }

    public Block getBlock(final byte column, final byte row) {
        return this.blocks[row][column];
    }

    public Block[] getRowOfBlocks(final byte row) {
        return this.blocks[row];
    }

    public Block[][] getTable() {
        return this.blocks;
    }
}