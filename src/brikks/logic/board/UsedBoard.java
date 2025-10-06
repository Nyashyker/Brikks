package brikks.logic.board;

import brikks.essentials.Block;
import brikks.essentials.PlacedBlock;
import brikks.essentials.Position;

import java.util.List;
import java.util.TreeSet;

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
        final TreeSet<Position> variants = new TreeSet<>();

        for (byte x = 0; x < this.width; x++) {
            for (byte y = 0; y < this.height; y++) {
                for (byte leftX = x; leftX >= 0; leftX--) {
                    if (this.canBePlaced(block, leftX, y)) {
                        variants.add(new Position(leftX, y));
                    }
                }
                for (byte rightX = x; rightX < this.width; rightX++) {
                    if (this.canBePlaced(block, rightX, y)) {
                        variants.add(new Position(rightX, y));
                    }
                }
                if (this.canBePlaced(block, x, y)) {
                    variants.add(new Position(x, y));
                }
            }
        }

        return variants.stream().toList();
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


    private boolean canBePlaced(final Block block, final byte x, final byte y) {
        boolean canBePlaced = false;
        for (final Position shape : block.getBlock()) {
            final Position shapePos = new Position((byte) (x + shape.getX()), (byte) (y + shape.getY()));
            // if (shapePos.getY() >= this.height) { throw new IllegalArgumentException("Blocks shape should be started from the bottom"); }

            if (shapePos.getY() < 0 || shapePos.getX() < 0 || shapePos.getX() >= this.width || this.used[shapePos.getY()][shapePos.getX()]) {
                return false;
            } else if (shapePos.getY() == this.height - 1 || this.used[shapePos.getY() + 1][shapePos.getX()]) {
                canBePlaced = true;
            }
        }
        return canBePlaced;
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
