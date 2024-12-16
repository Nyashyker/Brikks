package brikks.logic.board;

import brikks.essentials.*;
import brikks.essentials.enums.*;

public class BonusEnergyBoardD1 extends BonusEnergyBoard {
    public static final Level difficulty = Level.ONE;

    public BonusEnergyBoardD1(final byte width, final byte height) {
        super(width, height);
    }

    public BonusEnergyBoardD1(final Color[][] bonusEnergy) {
        super(bonusEnergy);
    }

    @Override
    public byte place(PlacedBlock block) {
        // TODO: implement
        return 0;
    }
}
