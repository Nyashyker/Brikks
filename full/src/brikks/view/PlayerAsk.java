package brikks.view;

import brikks.BlocksTable;
import brikks.essentials.*;
import brikks.view.enums.*;

public interface PlayerAsk {
    public boolean askReroll();
    public PlaceORSpecial askPlaceORSpecial();
    public Position askPlasingSpot(Block block, Position[] variants);
    public Special askSpecial();
    public Block askRotation(Block[] variants);
    public Block askChoice(BlocksTable variants);
}
