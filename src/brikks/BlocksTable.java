package brikks;

import brikks.essentials.Block;
import brikks.essentials.Position;
import brikks.essentials.enums.Color;

import java.util.ArrayList;
import java.util.List;

public class BlocksTable {
    public static final byte HEIGHT = 6;
    public static final byte WIDTH = 4;


    public static final Block duelBlock = new Block(new Position[]{new Position()}, Color.DUELER);

    private final Block[][] blocks;


    public BlocksTable() {
        this(BlocksTable.generateBlocksTable());
    }

    public BlocksTable(final Block[][] table) {
        if (table == null) {
            throw new NullPointerException("table is null");
        }
        if (table.length != BlocksTable.HEIGHT || table[0].length != BlocksTable.WIDTH) {
            throw new IllegalArgumentException("table has to be " + BlocksTable.WIDTH + " x " + BlocksTable.HEIGHT);
        }

        this.blocks = table;
    }

    private static Block[][] generateBlocksTable() {
        Block[][] blocks = new Block[BlocksTable.HEIGHT][BlocksTable.WIDTH];


        // White
        blocks[0][0] = mkBlock(
                "    " +
                        "    " +
                        " x  " +
                        "xxx "
                , Color.WHITE);
        blocks[0][1] = blocks[0][0].rotate();
        blocks[0][2] = blocks[0][1].rotate();
        blocks[0][3] = blocks[0][2].rotate();

        // Yellow
        blocks[1][0] = mkBlock(
                "    " +
                        " x  " +
                        " x  " +
                        "xx  "
                , Color.YELLOW);
        blocks[1][1] = blocks[1][0].rotate();
        blocks[1][2] = blocks[1][1].rotate();
        blocks[1][3] = blocks[1][2].rotate();

        // Green
        blocks[2][0] = mkBlock(
                "    " +
                        "x   " +
                        "x   " +
                        "xx  "
                , Color.GREEN);
        blocks[2][1] = blocks[2][0].rotate();
        blocks[2][2] = blocks[2][1].rotate();
        blocks[2][3] = blocks[2][2].rotate();

        // Red
        blocks[3][0] = mkBlock(
                "    " +
                        " x  " +
                        "xx  " +
                        "x   "
                , Color.RED);
        blocks[3][1] = blocks[3][0];
        blocks[3][2] = blocks[3][1].rotate();
        blocks[3][3] = blocks[3][2];

        // Blue
        blocks[4][0] = blocks[3][3].rotate(Color.BLUE);
        blocks[4][1] = blocks[4][0];
        blocks[4][2] = blocks[4][1].rotate();
        blocks[4][3] = blocks[4][2];

        // Black
        blocks[5][0] = mkBlock(
                "    " +
                        "    " +
                        "xx  " +
                        "xx  "
                , Color.BLACK);
        blocks[5][1] = blocks[5][0];
        blocks[5][2] = mkBlock(
                "    " +
                        "    " +
                        "    " +
                        "xxxx"
                , Color.BLACK);
        blocks[5][3] = blocks[5][2].rotate();


        return blocks;
    }


    private static Block mkBlock(final String shape, final Color color) {
        return new Block(mkShape(shape, 'x', Block.LEN), color);
    }

    private static Position[] mkShape(final String shape, final char color, final byte len) {
        final List<Position> block = new ArrayList<>();
        for (byte y = (byte) (len - 1); y >= 0; y--) {
            for (byte x = 0; x < len; x++) {
                if (shape.charAt(y * len + x) == color) {
                    block.add(new Position(x, (byte) (y - len + 1)));
                }
            }
        }

        return block.toArray(Position[]::new);
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


    public byte findOrigin(final Block block) {
        if (block.equals(BlocksTable.duelBlock)) {
            return BlocksTable.WIDTH * BlocksTable.HEIGHT;
        }

        for (byte row = 0; row < BlocksTable.HEIGHT; row++) {
            for (byte column = 0; column < BlocksTable.WIDTH; column++) {
                if (this.blocks[row][column].equals(block)) {
                    return (byte) (row * BlocksTable.WIDTH + column);
                }
            }
        }

        // Should not be reached
        throw new IllegalArgumentException("Unregistered block: " + block.toString());
    }
}
