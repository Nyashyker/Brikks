package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

public class EnergyBonusBoardD4 extends EnergyBonusBoard {
    public EnergyBonusBoardD4(final Color[][] energyBonus) {
        super(energyBonus);
    }


    @Override
    public byte place(PlacedBlock block) {
        byte bonusPoints = 0;

        for (final Position shapePos : block.getBlock()) {
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
