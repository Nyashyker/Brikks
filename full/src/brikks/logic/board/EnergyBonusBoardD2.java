package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

public class EnergyBonusBoardD2 extends EnergyBonusBoard {
    public EnergyBonusBoardD2(final Color[][] energyBonus) {
        super(energyBonus);
    }


    @Override
    public byte place(PlacedBlock block) {
        byte bonusPoints = 0;

        for (final Position shapePos : block.getBlock()) {
            final Color point = this.bonusEnergy[shapePos.getY()][shapePos.getX()];
            if (point != null && point == block.getColor()) {
                bonusPoints += 2;
            }
        }

        return bonusPoints;
    }
}
