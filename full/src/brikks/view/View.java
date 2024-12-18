package brikks.view;

import brikks.*;
import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.save.*;
import brikks.view.enums.*;

public abstract class View implements PlayerAsk, DuelAsk {
    abstract public Menu menu();
    abstract public void liderboard(PlayerLiderboard[] players);


    abstract public boolean askUseExistingPlayer(final String name);
    abstract public Level askDifficulty();
    abstract public Mode askMode();
    abstract public byte askPlayerCount(final byte maxPlayers);
    abstract public String askName();
    abstract public PlacedBlock[] sakFirstChoise(BlocksTable variants, Player[] players);


    abstract public void draw(Player player);
    abstract public void endSolo(Rank[] ranks, short finalScore);
    abstract public void endStandard(Player[] players);
    abstract public void endDuel(Player winner, Player loser);
    abstract public void exit();
}
