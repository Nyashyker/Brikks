package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.BonusScore;
import brikks.logic.Energy;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final byte WIDTH = 10;
    public static final byte HEIGHT = 11;

    public static final Block duelBlock = new Block(new Position[]{new Position()}, Color.DUELER);


    private final List<PlacedBlock> placed;

    private final BonusScore bonusScore;
    private final Energy energy;

    private final EnergyBonusBoard energyBonus;
    private final UsedBoard used;


    public Board(final BonusScore bonusScore, final Energy energy, final Level difficulty) {
        this(
                bonusScore,
                energy,
                difficulty,
                new ArrayList<>(),
                EnergyBonusBoard.generateEnergyBonus(Board.WIDTH, Board.HEIGHT)
        );
    }

    public Board(
            final BonusScore bonusScore,
            final Energy energy,
            final Level difficulty,
            final List<PlacedBlock> placed,
            final Color[][] energyBonus
    ) {
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
        for (final PlacedBlock check : placed) {
            if (check == null) {
                throw new IllegalArgumentException("No empty blocks in the placed");
            }
        }
        if (energyBonus == null) {
            throw new IllegalArgumentException("Bonus energy board cannot be null");
        }
        if (energyBonus.length != Board.HEIGHT || energyBonus[0].length != Board.WIDTH) {
            throw new IllegalArgumentException("BonusEnergyBoard must have the same length as Board");
        }


        this.placed = placed;

        this.bonusScore = bonusScore;
        this.energy = energy;

        this.energyBonus = EnergyBonusBoard.create(difficulty, energyBonus);
        this.used = new UsedBoard(Board.WIDTH, Board.HEIGHT, placed);
    }


    public List<PlacedBlock> getBoard() {
        return this.placed;
    }

    public Color[][] getEnergyBonus() {
        return this.energyBonus.bonusEnergy;
    }

    public byte getRowMultiplier(final byte y) {
        return switch (y) {
            case 0 -> 4;
            case 1, 2, 3, 4, 5 -> 2;
            default -> 1;
        };
    }


    public Position[] canBePlacedDuel() {
        List<Position> variants = this.used.canBePlaced(Board.duelBlock);

        List<Position> placedMiniblock = new ArrayList<>();
        for (final PlacedBlock placed : this.placed) {
            if (placed.getColor() == Color.DUELER) {
                placedMiniblock.add(placed.getPlace());
            }
        }

        for (byte i = 0; i < variants.size(); ) {
            boolean guessPlaceable = true;

            for (final Position check : placedMiniblock) {
                final byte distanceY = (byte) (Math.abs(variants.get(i).getY() - check.getY()));
                final byte distanceX = (byte) (Math.abs(variants.get(i).getX() - check.getX()));

                if (distanceY == 1 && distanceX == 0 || distanceY == 0 && distanceX == 1) {
                    guessPlaceable = false;
                    variants.remove(i);
                    break;
                }
            }

            if (guessPlaceable) {
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

        return (byte) (this.energy.grow(this.energyBonus.place(block)) + this.bonusScore.growByBoard(this.used.rowsFilled(block)));
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
