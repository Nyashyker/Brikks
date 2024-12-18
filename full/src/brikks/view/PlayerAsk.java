package brikks.view;

import brikks.BlocksTable;
import brikks.essentials.*;
import brikks.view.enums.*;

public interface PlayerAsk {
    boolean askReroll();
    boolean askUseSpecial();
    Position askPlacingSpot(Block block, Position[] variants);
    Special askSpecial();
    byte askRotation(Block[] variants);
    Position askChoice(BlocksTable variants);

    void failBomb();
    void failRotation();
    void failChoice();
}
