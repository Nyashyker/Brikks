package brikks.logic;

import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.board.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    public static final byte WIDTH = 10;
    public static final byte HEIGHT = 11;

    private static final Block duelBlock = new Block(new Position[0], Color.DUELER);


    private final List<PlacedBlock> placed;

    private final BonusScore bonusScore;
    private final Energy energy;

    private final BonusEnergyBoard bonusEnergy;
    private final UsedBoard used;


    public Board(final BonusScore bonusScore, final Energy energy, final Level difficulty) {
        if (bonusScore == null) {
            throw new IllegalArgumentException("Bonus score cannot be null");
        }
        if (energy == null) {
            throw new IllegalArgumentException("Energy score cannot be null");
        }
        if (difficulty == null) {
            throw new IllegalArgumentException("Difficulty cannot be null");
        }

        this.placed = new ArrayList<>();

        this.bonusScore = bonusScore;
        this.energy = energy;

        this.bonusEnergy = BonusEnergyBoard.create(difficulty, Board.WIDTH, Board.HEIGHT);
        this.used = new UsedBoard(Board.WIDTH, Board.HEIGHT);
    }

    public Board(final BonusScore bonusScore, final Energy energy, final Level difficulty,
                 final PlacedBlock[] placed, final Color[][] bonusEnergyBoard) {
        if (bonusScore == null) {
            throw new IllegalArgumentException("Bonus score cannot be null");
        }
        if (energy == null) {
            throw new IllegalArgumentException("Energy score cannot be null");
        }
        if (difficulty == null) {
            throw new IllegalArgumentException("Difficulty cannot be null");
        }
        if (placed == null) {
            throw new IllegalArgumentException("PlacedBlocks cannot be null");
        }
        for (PlacedBlock check : placed) {
            if (check == null) {
                throw new IllegalArgumentException("PlacedBlock cannot be null");
            }
        }
        if (bonusEnergyBoard == null) {
            throw new IllegalArgumentException("Bonus energy board cannot be null");
        }
        if (bonusEnergyBoard.length != Board.HEIGHT || bonusEnergyBoard[0].length != Board.WIDTH) {
            throw new IllegalArgumentException("BonusEnergyBoard must have the same length as Board");
        }


        this.placed = new ArrayList<>(Arrays.asList(placed));

        this.bonusScore = bonusScore;
        this.energy = energy;

        this.bonusEnergy = BonusEnergyBoard.create(difficulty, bonusEnergyBoard);
        this.used = new UsedBoard(Board.WIDTH, Board.HEIGHT, placed);
    }


    public PlacedBlock[] getBoard() {
        return this.placed.toArray(PlacedBlock[]::new);
    }

    public Color[][] getEnergyBonus() {
        return this.bonusEnergy.bonusEnergy;
    }

    public byte getRowMultiplier(final byte y) {
        return switch (y) {
            case 0 -> 4;
            case 1, 2, 3, 4, 5 -> 2;
            default -> 1;
        };
    }


    public Position[] canBePlaced() {
        List<Position> variants = this.used.canBePlaced(Board.duelBlock);

        // TODO: mozxe, zberigaty okremo?
        List<Position> placedMiniblock = new ArrayList<>();
        for (PlacedBlock placed : this.placed) {
            if (placed.getColor() == Color.DUELER) {
                placedMiniblock.add(placed.getPlace());
            }
        }

        for (byte i = 0; i < variants.size();) {
            boolean guesPlacable = true;

            for (Position check : placedMiniblock) {
                final byte distanceY = (byte) (Math.abs(variants.get(i).getY() - check.getY()));
                final byte distanceX = (byte) (Math.abs(variants.get(i).getX() - check.getX()));

                if (distanceY == 1 && distanceX == 0 || distanceY == 0 && distanceX == 1) {
                    guesPlacable = false;
                    variants.remove(i);
                    break;
                }
            }

            if (guesPlacable) {
                i++;
            }
        }

        return variants.toArray(Position[]::new);
    }

    public Position[] canBePlaced(final Block block) {
        return this.used.canBePlaced(block).toArray(Position[]::new);
    }

    public byte place(final PlacedBlock block) {
        this.used.place(block);
        this.placed.add(block);

        return (byte) (
                this.energy.grow(this.bonusEnergy.place(block)) +
                        this.bonusScore.growByBoard(this.used.rowsFilled(block))
        );
    }

    public void opponentsPlace(final Position position) {
        final PlacedBlock miniblock = new PlacedBlock(Board.duelBlock, position);

        this.used.place(miniblock);
        this.placed.add(miniblock);
    }

    public short calculateRow(final byte y) {
        final short points = switch (this.used.countRowsGaps(y)) {
            case 0 -> 5;
            case 1 -> 2;
            case 2 -> 1;
            default -> 0;
        };

        return (short) (points * this.getRowMultiplier(y));
    }

    public short calculateFinal() {
        short score = 0;

        for (byte y = 0; y < Board.HEIGHT; y++) {
            score += this.calculateRow(y);
        }

        return score;
    }
}
