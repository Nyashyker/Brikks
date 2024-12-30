package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

public class BonusEnergyBoardD4 extends BonusEnergyBoard {
    private static final Level difficulty = Level.FOUR;


    public BonusEnergyBoardD4(final byte width, final byte height) {
        super(width, height);
    }

    public BonusEnergyBoardD4(final Color[][] bonusEnergy) {
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

            final Color point = this.bonusEnergy[shapePos.getY()][shapePos.getX()];
            if (point != null) {
                if (point == block.getColor()) {
                    bonusPoints += 2;
                } else {
                    bonusPoints -= 2;
                }
            }
        }

        return bonusPoints;
    }
}
