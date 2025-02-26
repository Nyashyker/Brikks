package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

class EnergyBonusBoardD3 extends EnergyBonusBoard {
    EnergyBonusBoardD3(final Color[][] energyBonus) {
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
                    bonusPoints -= 1;
                }
            }
        }

        return bonusPoints;
    }
}
