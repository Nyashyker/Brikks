package brikks;

import brikks.essentials.*;
import brikks.essentials.enums.Color;

public class BlocksTable {
    public static final byte HEIGHT = 6;
    public static final byte WIDTH = 4;

    public static final Block duelBlock = new Block(new Position[]{new Position()}, Color.DUELER);

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


    public Position findOrigin(final Block block) {
        if (block.equals(BlocksTable.duelBlock)) {
            return null;
        }

        for (byte row = 0; row < BlocksTable.HEIGHT; row++) {
            for (byte column = 0; column < BlocksTable.WIDTH; column++) {
                if (this.blocks[row][column].equals(block)) {
                    return new Position(column, row);
                }
            }
        }

        // Should not be reached
        return null;
    }
}
