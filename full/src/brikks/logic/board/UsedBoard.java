package brikks.logic.board;

import brikks.essentials.*;

import java.util.LinkedList;
import java.util.List;

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

    public UsedBoard(final byte width, final byte height, final PlacedBlock[] placed) {
        this(width, height);

        if (placed == null) {
            throw new IllegalArgumentException("placed must not be null");
        }

        for (final PlacedBlock block : placed) {
            this.place(block);
        }
    }


    public List<Position> canBePlaced(final Block block) {
        List<Position> variants = new LinkedList<Position>();

        for (byte x = 0; x < used[0].length; x++) {
            Position lastValid = null;

            for (byte y = (byte) (used.length - 1); y >= 0; y--) {
                boolean guessValid = !this.used[y][x];

                if (guessValid) {
                    for (final Position shape : block.getBlock()) {
                        final Position shapePos = new Position((byte) (x + shape.getX()), (byte) (y + shape.getY()));

                        if (this.isPositionUnsuitable(shapePos)) {
                            guessValid = false;
                            break;
                        }
                    }
                }

                if (guessValid) {
                    lastValid = new Position(x, y);
                } else {
                    break;
                }
            }

            if (lastValid != null) {
                variants.add(lastValid);
            }
        }

        return variants;
    }

    public void place(final PlacedBlock block) {
        for (final Position shapePos : block.getBlock()) {
            if (this.isPositionUnsuitable(shapePos)) {
                throw new IllegalArgumentException("Block " + block + " can not be placed");
            }

            this.used[shapePos.getY()][shapePos.getX()] = true;
        }
    }

    public byte rowsFilled(final PlacedBlock block) {
        byte count = 0;

        byte lastY = -1;
        for (final Position shapePos : block.getBlock()) {
            if (this.isPositionUnsuitable(shapePos)) {
                throw new IllegalArgumentException("Block " + block + " is not placed");
            }

            if (shapePos.getY() != lastY && this.isRowFilled(shapePos.getY())) {
                lastY = shapePos.getY();
                count++;
            }
        }

        return count;
    }

    public byte countRowsGaps(final byte y) {
        byte gaps = 0;

        for (byte x = 0; x < this.used[y].length; x++) {
            if (!this.used[y][x]) {
                gaps++;
            }
        }

        return gaps;
    }


    private boolean isRowFilled(final byte y) {
        for (byte x = 0; x < this.used[y].length; x++) {
            if (!this.used[y][x]) { return false; }
        }

        return true;
    }

    private boolean isPositionUnsuitable(final Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Block shape must not be null");
        }

        return position.getY() < 0 || position.getY() >= this.used.length || position.getX() < 0 || position.getX() >= this.used[0].length || this.used[position.getY()][position.getX()];
    }
}
