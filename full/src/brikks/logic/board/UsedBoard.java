package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

public class UsedBoard {
    public final boolean[][] used;


    public UsedBoard(final byte width, final byte height) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be greater than 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height must be greater than 0");
        }

        boolean[][] used = new boolean[height][width];
        for (int y = 0; y < height; ++y) {
            used[y] = new boolean[width];
            for (int x = 0; x < width; ++x) {
                used[y][x] = false;
            }
        }

        this.used = used;
    }

    public UsedBoard(boolean[][] used) {
        if (used.length == 0 || used[0].length == 0) {
            throw new IllegalArgumentException("used cannot be empty");
        }

        this.used = used;
    }


    public Position[] canBePlaced(final Block block) {
        // TODO: implement
        return null;
    }

    public void place(final PlacedBlock block) {
        Position last = new Position((byte) 0, (byte) 0);
        for (Position shape : block.getBlock()) {
            last = new Position((byte) (last.getX() + shape.getX()), (byte) (last.getY() + shape.getY()));

            if (this.used[last.getY()][last.getX()]) {
                throw new IllegalArgumentException("Block " + block + " can not be placed");
            }

            this.used[last.getY()][last.getX()] = true;
        }
    }

    public byte rowsFilled(final PlacedBlock block) {
        byte count = 0;

        Position last = new Position((byte) 0, (byte) 0);
        for (Position shape : block.getBlock()) {
            last = new Position((byte) (last.getX() + shape.getX()), (byte) (last.getY() + shape.getY()));

            if (!this.used[last.getY()][last.getX()]) {
                throw new IllegalArgumentException("Block " + block + " is not placed");
            }

            if (shape.getY() != 0 && this.isRowFilled(last.getY())) {
                count++;
            }
        }

        // One pretty exceptional situation
        if (block.getBlock()[0].getY() == 0 && this.isRowFilled(block.getBlock()[0].getY())) {
            count++;
        }

        return count;
    }

    public byte rowsAlmostFilled(final byte exactTolerance) {
        byte count = 0;

        for (byte y = 0; y < this.used.length; y++) {
            if (this.isRowFilled(y, exactTolerance)) {
                count++;
            }
        }

        return count;
    }


    // TODO: validate placement

    private boolean isRowFilled(final byte y, byte exactTolerance) {
        for (byte x = 0; x < this.used[y].length; x++) {
            if (!this.used[y][x]) {
                exactTolerance--;
            }
        }

        return exactTolerance == 0;
    }

    private boolean isRowFilled(final byte y) {
        return this.isRowFilled(y, (byte) 0);
    }
}
