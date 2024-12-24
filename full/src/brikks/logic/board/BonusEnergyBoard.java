package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.Board;

import java.util.Random;

public abstract class BonusEnergyBoard {
    private static Level difficulty;

    public static BonusEnergyBoard create(final Level difficulty, final byte width, final byte height) {
        return switch (difficulty) {
            case ONE -> new BonusEnergyBoardD1(width, height);
            case TWO -> new BonusEnergyBoardD2(width, height);
            case THREE -> new BonusEnergyBoardD3(width, height);
            case FOUR -> new BonusEnergyBoardD4(width, height);
        };
    }

    public static BonusEnergyBoard create(final Level difficulty, final Color[][] bonusEnergy) {
        return switch (difficulty) {
            case ONE -> new BonusEnergyBoardD1(bonusEnergy);
            case TWO -> new BonusEnergyBoardD2(bonusEnergy);
            case THREE -> new BonusEnergyBoardD3(bonusEnergy);
            case FOUR -> new BonusEnergyBoardD4(bonusEnergy);
        };
    }


    public final Color[][] bonusEnergy;


    protected BonusEnergyBoard(final byte width, final byte height) {
        this(generateBonusEnergy(width, (byte) (height)));
    }

    protected BonusEnergyBoard(final Color[][] bonusEnergy) {
        if (bonusEnergy == null) {
            throw new IllegalArgumentException("bonusEnergy cannot be null");
        }
        if (bonusEnergy.length == 0 || bonusEnergy[0].length == 0) {
            throw new IllegalArgumentException("bonusEnergy cannot be empty");
        }

        this.bonusEnergy = bonusEnergy;
    }

    protected static Color[][] generateBonusEnergy(final byte width, final byte height) {
        final Random rand = new Random();

        final byte maxHeight = (byte) (height - Block.LEN / 2);

        final Color[][] bonusEnergy = new Color[height][height];
        for (byte y = 0; y < height; y++) {
            bonusEnergy[y] = new Color[width];
        }

        for (byte _i = 0; _i < 2; _i++) {
            for (Color color : Color.values()) {
                if (color == Color.DUELER) {
                    continue;
                }

                Position place = new Position((byte) 0, (byte) 0);
                do {
                    place.setY((byte) rand.nextInt(maxHeight));
                    place.setX((byte) rand.nextInt(width));
                } while (bonusEnergy[place.getY()][place.getX()] != null);
                bonusEnergy[place.getY()][place.getX()] = color;
            }
        }

/*
        bonusEnergy[0][6] = Color.GREEN;
        bonusEnergy[1][0] = Color.RED;
        bonusEnergy[1][9] = Color.BLUE;
        bonusEnergy[2][3] = Color.BLACK;
        bonusEnergy[3][7] = Color.WHITE;
        bonusEnergy[4][1] = Color.YELLOW;
        bonusEnergy[5][5] = Color.RED;
        bonusEnergy[6][2] = Color.BLUE;
        bonusEnergy[6][9] = Color.YELLOW;
        bonusEnergy[7][0] = Color.BLACK;
        bonusEnergy[7][7] = Color.GREEN;
        bonusEnergy[8][4] = Color.WHITE;
*/

        return bonusEnergy;
    }


    abstract public Level getDifficulty();


    abstract public byte place(PlacedBlock block);


    protected void validatePosition(final Position position) {
        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }
        if (position.getY() < 0 || position.getY() >= this.bonusEnergy.length || position.getX() < 0 || position.getX() >= this.bonusEnergy[0].length) {
            throw new IllegalArgumentException("position must be between 0 and " + (this.bonusEnergy.length - 1));
        }
    }
}
