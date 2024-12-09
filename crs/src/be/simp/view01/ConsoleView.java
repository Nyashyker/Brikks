package be.simp.view01;


import be.kdg.integration.brikks_project.*;
import be.simp.Block;
import be.simp.Level;
import be.simp.Mode;
import be.simp.*;
import be.simp.Position;


public class ConsoleView extends View {

    public GameText text;

    public Level askDifficulty();
    public Mode askMode();
    public String[] askNames();
    public Block[] askFirstChoice(BlocksTable variants, Player[] players);

    public void draw(Board board, Energy energy, Bombs bombs, BonusScore bonusScore);


    public boolean askReroll();
    public PlaceORSpesial askPlaceOrSpecial();
    public Position askPlacingSpot(Block block, Position[] variants);
    public Position askPlacingMiniblock(Board opponentsBoard, Position[] variants);
    public Special askSpecial();
    public Block askRotation(Block[] variants);
    public Block askChoice(BlocksTable variants);

    public void endPlayer(Player player);
    public void end(Player[] player);

    public void start();
    public void exit();



}
