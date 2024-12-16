package brikks.logic.board;

import brikks.essentials.PlacedBlock;
import brikks.essentials.enums.Color;
import brikks.essentials.enums.Level;

public class BonusEnergyBoardD4 extends BonusEnergyBoard {
    public static final Level difficulty = Level.FOUR;

    public BonusEnergyBoardD4(final byte width, final byte height) {
        super(width, height);
    }

    public BonusEnergyBoardD4(final Color[][] bonusEnergy) {
        super(bonusEnergy);
    }

    @Override
    public byte place(PlacedBlock block) {
        // TODO: implement
        return 0;
    }
}
