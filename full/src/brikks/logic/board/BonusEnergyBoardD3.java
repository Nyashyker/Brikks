package brikks.logic.board;

import brikks.essentials.PlacedBlock;
import brikks.essentials.enums.Color;
import brikks.essentials.enums.Level;

public class BonusEnergyBoardD3 extends BonusEnergyBoard {
    public static final Level difficulty = Level.THREE;

    public BonusEnergyBoardD3(final byte width, final byte height) {
        super(width, height);
    }

    public BonusEnergyBoardD3(final Color[][] bonusEnergy) {
        super(bonusEnergy);
    }

    @Override
    public byte place(PlacedBlock block) {
        // TODO: implement
        return 0;
    }
}
