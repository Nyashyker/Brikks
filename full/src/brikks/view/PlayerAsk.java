package brikks.view;

import brikks.BlocksTable;
import brikks.essentials.*;
import brikks.view.enums.*;

public interface PlayerAsk {
    boolean askReroll(final Block block);

    // TODO: placing spot mozxe buty null
    Position askPlacingSpot(final Block block, final Position[] variants);

    Doing askDoing(final BlocksTable blocks, final Position roll);

    byte askRotation(final Block[] variants);

    Position askChoice(final BlocksTable variants);


    void successPlace(final PlacedBlock placed);

    void successBomb();

    void successRotation(final byte energyCost);

    void successChoice(final byte energyCost);


    void failPlace();

    void failBomb();

    void failRotation();

    void failChoice();

    void failGiveUp();

    void fail();
}
