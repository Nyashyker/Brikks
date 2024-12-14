package brikks.view;

import brikks.BlocksTable;
import brikks.Player;
import brikks.essentials.PlacedBlock;
import brikks.essentials.enums.Level;
import brikks.view.enums.Menu;
import brikks.essentials.enums.Mode;
import brikks.logic.Board;
import brikks.logic.Bombs;
import brikks.logic.BonusScore;
import brikks.logic.Energy;
import brikks.save.PlayerLiderboard;

public abstract class View implements PlayerAsk, DuelAsk {
    public abstract Menu menu();
    public abstract void liderboard(PlayerLiderboard[] players);
    public abstract Level askDifficulty();
    public abstract Mode askMode();
    public abstract String[] askNames();
    public abstract boolean askToSave();
    public abstract PlacedBlock[] sakFirstChoise(BlocksTable variants, Player[] players);
    public abstract void draw(Board board, Energy energy, Bombs bombs, BonusScore bonusScore);
    public abstract void endPlayer(Player player);
    public abstract void end(Player[] players);
    public abstract void start();
    public abstract void exit();
}
