package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

public class BonusEnergyBoardD2 extends BonusEnergyBoard {
    public static final Level difficulty = Level.TWO;


    public BonusEnergyBoardD2(final byte width, final byte height) {
        super(width, height);
    }

    public BonusEnergyBoardD2(final Color[][] bonusEnergy) {
        super(bonusEnergy);
    }


    @Override
    public Level getDifficulty() {
        return difficulty;
    }


    @Override
    public byte place(PlacedBlock block) {
        byte bonusPoints = 0;

        for (Position shapePos : block.getBlock()) {
            this.validatePosition(shapePos);

            final Color point = this.bonusEnergy[shapePos.getY()][shapePos.getY()];
            if (point != null && point == block.getColor()) {
                bonusPoints += 2;
            }
        }

        return bonusPoints;
    }
}
