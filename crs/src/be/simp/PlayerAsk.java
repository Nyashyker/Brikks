package be.simp;

import be.kdg.integration.brikks_project.BlocksTable;


public interface PlayerAsk {

    public boolean askReroll();
    public PlaceORSpecial askRoll();
    public Position askPlacingSpot(Block block, Position[] variants);
    public Special askSpecial();
    public Block askRotation(Block[] variants);
    public Block askChoice(BlocksTable variants);



}
