package view01;


import java.awt.print.Printable;

public abstract class View implements Printable {

    public abstract Menu menu();
    public abstract void liderboard(PlayerLiderboard[] players);

    public abstract Level askDifficulty();
    public abstract Mode askMode();
    public abstract String[] askNames();
    public abstract PlacedBlock[] askFirstChoice(BlockTable variants, Player[] players);


    public abstract void draw(Board board, Energy energy, Bombs bombs, BonusScore bonusScore);

    public abstract void endPlayer(Player player);
    public abstract void end(Player[] players);
    public abstract void start();
    public abstract void exit();

}
