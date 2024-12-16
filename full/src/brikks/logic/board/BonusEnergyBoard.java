package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.Board;

import java.util.Random;

public abstract class BonusEnergyBoard {
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

    abstract public byte place(PlacedBlock block);
}
