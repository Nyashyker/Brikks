package brikks.logic.board;

import brikks.essentials.*;

import java.util.ArrayList;
import java.util.List;

class UsedBoard {
    private final byte width;
    private final byte height;
    final boolean[][] used;


    UsedBoard(final byte width, final byte height) {
        boolean[][] used = new boolean[height][width];
        for (int y = 0; y < height; ++y) {
            used[y] = new boolean[width];
            for (int x = 0; x < width; ++x) {
                used[y][x] = false;
            }
        }

        this.width = width;
        this.height = height;
        this.used = used;
    }

    UsedBoard(final byte width, final byte height, final List<PlacedBlock> placed) {
        this(width, height);

        for (final PlacedBlock block : placed) {
            this.place(block);
        }
    }


    List<Position> canBePlaced(final Block block) {
        List<Position> variants = new ArrayList<>();

        for (byte x = 0; x < this.width; x++) {
            Position lastValid = null;

            boolean noMoreOptions = false;
            for (byte y = 0; y < this.height; y++) {
                boolean guessValid = true;

                for (final Position shape : block.getBlock()) {
                    final Position shapePos = new Position((byte) (x + shape.getX()), (byte) (y + shape.getY()));
                    if (shapePos.getY() >= this.height) {
                        throw new IllegalArgumentException("Blocks shape should be started from the bottom");
                    }

                    if (shapePos.getY() < 0) {
                        guessValid = false;
                        break;
                    }
                    if (shapePos.getX() < 0 || shapePos.getX() >= this.width || this.used[shapePos.getY()][shapePos.getX()]) {
                        guessValid = false;
                        noMoreOptions = true;
                        break;
                    }
                }

                if (noMoreOptions) {
                    break;
                } else if (guessValid) {
                    lastValid = new Position(x, y);
                }
            }

            if (lastValid != null) {
                variants.add(lastValid);
            }
        }

        // TODO: support placing block under other blocks if there is space for them to fall this way

        return variants;
    }

    void place(final PlacedBlock block) {
        for (final Position shapePos : block.getBlock()) {
            if (shapePos.getY() < 0 || shapePos.getY() >= this.height || shapePos.getX() < 0 || shapePos.getX() >= this.width || this.used[shapePos.getY()][shapePos.getX()]) {
                throw new IllegalArgumentException("Block " + block + " can not be placed");
            }

            this.used[shapePos.getY()][shapePos.getX()] = true;
        }
    }

    byte rowsFilled(final PlacedBlock block) {
        byte count = 0;

        byte lastY = -1;
        for (final Position shapePos : block.getBlock()) {
            if (shapePos.getY() < 0 || shapePos.getY() >= this.height || shapePos.getX() < 0 || shapePos.getX() >= this.width || !this.used[shapePos.getY()][shapePos.getX()]) {
                throw new IllegalArgumentException("Block " + block + " is not placed");
            }

            if (shapePos.getY() != lastY && this.isRowFilled(shapePos.getY())) {
                lastY = shapePos.getY();
                count++;
            }
        }

        return count;
    }

    byte countRowsGaps(final byte y) {
        byte gaps = 0;

        for (byte x = 0; x < this.width; x++) {
            if (!this.used[y][x]) {
                gaps++;
            }
        }

        return gaps;
    }


    private boolean isRowFilled(final byte y) {
        for (byte x = 0; x < this.width; x++) {
            if (!this.used[y][x]) {
                return false;
            }
        }

        return true;
    }
}
