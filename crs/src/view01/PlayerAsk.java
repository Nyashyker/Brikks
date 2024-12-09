package view01;

public interface PlayerAsk {

    public boolean askReroll();
    public PlaceOrSpesial askRoll();
    public Position askPlasingSpot(Block block, Position[] variants);
    public Special askSpecial();
    public Block askRotation(Block[] variants);
    public Block askChoice(BlocksTable variants);



}
