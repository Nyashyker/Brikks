package be.simp.view01;

import be.kdg.integration.brikks_project.BlocksTable;
import be.simp.Block;
import be.simp.PlaceORSpecial;
import be.simp.Position;
import be.simp.Special;


public interface PlayerAsk {

    public boolean askReroll();
    public PlaceORSpecial askRoll();
    public Position askPlasingSpot(Block block, Position[] variants);
    public Special askSpecial();
    public Block askRotation(Block[] variants);
    public Block askChoice(BlocksTable variants);



}
