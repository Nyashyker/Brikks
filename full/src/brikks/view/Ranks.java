package brikks.view;

import brikks.essentials.enums.Level;

public class Ranks {
    public static byte getRank(final Level difficulty, final short finalScore) {
        final byte difficultyBuff = switch (difficulty) {
            case ONE -> 0;
            case TWO -> 5;
            case THREE -> 10;
            case FOUR -> 15;
            default -> throw new IllegalStateException("Unexpected difficulty");
        };

        if (finalScore < 60 - difficultyBuff) {
            return 0;
        } else if (finalScore < 70 - difficultyBuff) {
            return 1;
        } else if (finalScore < 80 - difficultyBuff) {
            return 2;
        } else if (finalScore < 90 - difficultyBuff) {
            return 3;
        } else if (finalScore < 100 - difficultyBuff) {
            return 4;
        } else if (finalScore < 110 - difficultyBuff) {
            return 5;
        } else if (finalScore < 130 - difficultyBuff) {
            return 6;
        } else if (finalScore < 150 - difficultyBuff) {
            return 7;
        } else {
            return 8;
        }
    }
}
