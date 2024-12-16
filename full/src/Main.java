import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.*;

import java.util.LinkedList;
import java.util.List;


public class Main {
    private static final byte BLOCK_LEN = 4;

    public static void main(String[] args) {
/*
        Block[][] table = generateBlocksTable();
        for (byte y = 0; y < table.length; y++) {
            for (byte x = 0; x < table[y].length; x++) {
                System.out.println(table[y][x]);
                System.out.println();
            }
            System.out.println("--------");
        }
*/
    }

    private static Block[][] generateBlocksTable() {
        Block[][] blocks = new Block[BlocksTable.HEIGHT][BlocksTable.WIDTH];


        // White
        blocks[0][0] = new Block(
                mkShape("    " +
                        "    " +
                        " x  " +
                        "xxx "
                ), Color.WHITE);

        blocks[0][1] = new Block(
                mkShape("    " +
                        " x  " +
                        " xx " +
                        " x  "
                ), Color.WHITE);

        blocks[0][2] = new Block(
                mkShape("    " +
                        "    " +
                        "xxx " +
                        " x  "
                ), Color.WHITE);

        blocks[0][3] = new Block(
                mkShape("    " +
                        " x  " +
                        "xx  " +
                        " x  "
                ), Color.WHITE);

        // Yellow
        blocks[1][0] = new Block(
                mkShape("    " +
                        " x  " +
                        " x  " +
                        "xx  "
                ), Color.YELLOW);

        blocks[1][1] = new Block(
                mkShape("    " +
                        "    " +
                        "x   " +
                        "xxx "
                ), Color.YELLOW);

        blocks[1][2] = new Block(
                mkShape("    " +
                        "xx  " +
                        "x   " +
                        "x   "
                ), Color.YELLOW);

        blocks[1][3] = new Block(
                mkShape("    " +
                        "    " +
                        "xxx " +
                        "  x "
                ), Color.YELLOW);

        // Green
        blocks[2][0] = new Block(
                mkShape("    " +
                        "x   " +
                        "x   " +
                        "xx  "
                ), Color.GREEN);

        blocks[2][1] = new Block(
                mkShape("    " +
                        "    " +
                        "xxx " +
                        "x   "
                ), Color.GREEN);

        blocks[2][2] = new Block(
                mkShape("    " +
                        "xx  " +
                        " x  " +
                        " x  "
                ), Color.GREEN);

        blocks[2][3] = new Block(
                mkShape("    " +
                        "    " +
                        "  x " +
                        "xxx "
                ), Color.GREEN);

        // Red
        blocks[3][0] = new Block(
                mkShape("    " +
                        " x  " +
                        "xx  " +
                        "x   "
                ), Color.RED);

        blocks[3][1] = new Block(
                mkShape("    " +
                        " x  " +
                        "xx  " +
                        "x   "
                ), Color.RED);

        blocks[3][2] = new Block(
                mkShape("    " +
                        "    " +
                        "xx  " +
                        " xx "
                ), Color.RED);

        blocks[3][3] = new Block(
                mkShape("    " +
                        "    " +
                        "xx  " +
                        " xx "
                ), Color.RED);

        // Blue
        blocks[4][0] = new Block(
                mkShape("    " +
                        "x   " +
                        "xx  " +
                        " x  "
                ), Color.BLUE);

        blocks[4][1] = new Block(
                mkShape("    " +
                        "x   " +
                        "xx  " +
                        " x  "
                ), Color.BLUE);

        blocks[4][2] = new Block(
                mkShape("    " +
                        "    " +
                        " xx " +
                        "xx  "
                ), Color.RED);

        blocks[4][3] = new Block(
                mkShape("    " +
                        "    " +
                        " xx " +
                        "xx  "
                ), Color.BLUE);

        // Black
        blocks[5][0] = new Block(
                mkShape("    " +
                        "    " +
                        "xx  " +
                        "xx  "
                ), Color.BLUE);

        blocks[5][1] = new Block(
                mkShape("    " +
                        "    " +
                        "xx  " +
                        "xx  "
                ), Color.BLUE);

        blocks[5][2] = new Block(
                mkShape("    " +
                        "    " +
                        "    " +
                        "xxxx"
                ), Color.BLUE);

        blocks[5][3] = new Block(
                mkShape("x   " +
                        "x   " +
                        "x   " +
                        "x   "
                ), Color.BLUE);


        return blocks;
    }

    private static Position[] mkShape(final String shape) {
        final char color = 'x';
        Position last = null;

        for (byte x = 0; x < BLOCK_LEN; x++) {
            if (shape.charAt((BLOCK_LEN - 1) * BLOCK_LEN + x) == color) {
                last = new Position(x, (byte) (BLOCK_LEN - 1));
                break;
            }
        }
        // Error
        if (last == null) {
            throw new IllegalArgumentException("Shape " + shape + " not found\nUse " + color + " to describe the shape");
        }

        final List<Position> block = new LinkedList<Position>();
        for (byte y = BLOCK_LEN - 1; y >= 0; y--) {
            for (byte x = 0; x < BLOCK_LEN; x++) {
                if (shape.charAt(y * BLOCK_LEN + x) == color) {
                    block.add(new Position((byte) (x - last.getX()), (byte) (y - last.getY())));
                    last = new Position(x, y);
                }
            }
        }
        block.removeFirst();

        return block.toArray(Position[]::new);
    }
}
