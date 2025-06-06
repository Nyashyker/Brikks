package brikks.view;

import brikks.Player;
import brikks.essentials.Block;
import brikks.essentials.PlacedBlock;
import brikks.essentials.Position;
import brikks.view.enums.Deed;

public interface PlayerAsk {
    boolean askReroll(final Block block);

    Position askPlacingSpot(final Player player, final Block block, final Position[] variants);

    Deed askDeed(final Block block);

    byte askRotation(final Block[] variants);

    Position askChoice(final Block[][] variants);


    void successPlace(final Player player, final PlacedBlock placed);

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
