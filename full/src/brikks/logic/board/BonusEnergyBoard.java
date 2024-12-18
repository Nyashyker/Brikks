package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

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
        this(generateBonusEnergy(width, height));
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

        final Color[][] bonusEnergy = new Color[height][height];
        for (byte y = 0; y < height; y++) {
            for (byte x = 0; x < width; x++) {
                int r = rand.nextInt(Color.values().length) * 10;
                if (r < Color.values().length) {
                    bonusEnergy[y][x] = Color.values()[r];
                } else {
                    bonusEnergy[y][x] = null;
                }
            }
        }

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
