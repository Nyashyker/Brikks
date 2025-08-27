package brikks.logic.board;

import brikks.essentials.Block;
import brikks.essentials.PlacedBlock;
import brikks.essentials.Position;
import brikks.essentials.enums.Color;
import brikks.essentials.enums.Level;

import java.util.Random;

class EnergyBonusBoard {
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


    private final byte rightColorBonus;
    private final byte wrongColorBonus;
    final Color[][] bonusEnergy;


    EnergyBonusBoard(final Level difficulty, final Color[][] energyBonus) {
        switch (difficulty) {
            case ONE -> {
                this.rightColorBonus = 2;
                this.wrongColorBonus = 1;
            }
            case TWO -> {
                this.rightColorBonus = 2;
                this.wrongColorBonus = 0;
            }
            case THREE -> {
                this.rightColorBonus = 2;
                this.wrongColorBonus = -1;
            }
            case FOUR -> {
                this.rightColorBonus = 2;
                this.wrongColorBonus = -2;
            }
            case null -> throw new IllegalArgumentException("Difficulty can not be null!");
            default -> throw new IllegalArgumentException("Unexpected difficulty level");
        }

        this.bonusEnergy = energyBonus;
    }


    byte place(final PlacedBlock block) {
        byte bonusPoints = 0;

        for (final Position shapePos : block.getBlock()) {
            final Color point = this.bonusEnergy[shapePos.getY()][shapePos.getX()];
            if (point != null) {
                if (point == block.getColor()) {
                    bonusPoints += this.rightColorBonus;
                } else {
                    bonusPoints += this.wrongColorBonus;
                }
                this.bonusEnergy[shapePos.getY()][shapePos.getX()] = null;
            }
        }

        return bonusPoints;
    }
}
