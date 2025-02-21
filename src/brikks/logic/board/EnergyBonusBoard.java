package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

import java.util.Random;

abstract class EnergyBonusBoard {
    static EnergyBonusBoard create(final Level difficulty, final Color[][] energyBonus) {
        return switch (difficulty) {
            case ONE -> new EnergyBonusBoardD1(energyBonus);
            case TWO -> new EnergyBonusBoardD2(energyBonus);
            case THREE -> new EnergyBonusBoardD3(energyBonus);
            case FOUR -> new EnergyBonusBoardD4(energyBonus);
        };
    }

    static Color[][] generateEnergyBonus(final byte width, final byte height) {
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

                Position place = new Position();
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


    public final Color[][] bonusEnergy;


    protected EnergyBonusBoard(final Color[][] energyBonus) {
        this.bonusEnergy = energyBonus;
    }


    // TODO: clear the space after block overlapped
    abstract public byte place(PlacedBlock block);
}
