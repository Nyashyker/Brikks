package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

public class BonusEnergyBoardD3 extends BonusEnergyBoard {
    public static final Level difficulty = Level.THREE;


    public BonusEnergyBoardD3(final byte width, final byte height) {
        super(width, height);
    }

    public BonusEnergyBoardD3(final Color[][] bonusEnergy) {
        super(bonusEnergy);
    }


    @Override
    public Level getDifficulty() {
        return difficulty;
    }


    @Override
    public byte place(PlacedBlock block) {
        byte bonusPoints = 0;

        for (final Position shapePos : block.getBlock()) {
            this.validatePosition(shapePos);

            final Color point = this.bonusEnergy[shapePos.getY()][shapePos.getY()];
            if (point != null) {
                if (point == block.getColor()) {
                    bonusPoints += 2;
                } else {
                    bonusPoints -= 1;
                }
            }
        }

        return bonusPoints;
    }
}
