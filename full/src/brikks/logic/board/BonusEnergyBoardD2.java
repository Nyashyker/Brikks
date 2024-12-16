package brikks.logic.board;

import brikks.essentials.PlacedBlock;
import brikks.essentials.enums.Color;
import brikks.essentials.enums.Level;

public class BonusEnergyBoardD2 extends BonusEnergyBoard {
    public static final Level difficulty = Level.TWO;

    public BonusEnergyBoardD2(final byte width, final byte height) {
        super(width, height);
    }

    public BonusEnergyBoardD2(final Color[][] bonusEnergy) {
        super(bonusEnergy);
    }

    @Override
    public byte place(PlacedBlock block) {
        // TODO: implement
        return 0;
    }
}
