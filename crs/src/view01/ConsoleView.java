package view01;


public class ConsoleView extends View {

    public GameText text;

    public Level askDifficulty();
    public Mode askMode();
    public String[] askNames();
    public Block[] askFirstChoice(BlockTable variants, Player[] players);

    public void draw(Board board, Energy energy, Bombs bombs, BonusScore bonusScore);


    public boolean askReroll();
    public PlaceOrSpesial askPlaceOrSpecial();
    public Position askPlacingSpot(Block block, Position[] variants);
    public Position askPlacingMiniblock(Board opponentsBoard, Position[] variants);
    public Special askSpecial();
    public Block askRotation(Block[] variants);
    public Block askChoice(BlockTable variants);

    public void endPlayer(Player player);
    public void end(Player[] player);

    public void start();
    public void exit();







}
