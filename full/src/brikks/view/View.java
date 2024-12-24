package brikks.view;

import brikks.*;
import brikks.essentials.enums.*;
import brikks.save.container.*;
import brikks.view.enums.*;

public abstract class View implements PlayerAsk, DuelAsk {
    abstract public Menu menu();
    abstract public void liderboard(final PlayerLiderboard[] players);


    abstract public boolean askUseExistingPlayer(final String name);
    abstract public Level askDifficulty();
    abstract public boolean askDuel();
    abstract public byte askPlayerCount(final byte maxPlayers);
    abstract public String askName();


    abstract public SavedGame askChoiceSave(final SavedGame[] variants);


    abstract public void draw(final Player player);
    abstract public void endSolo(final String name, final short finalScore, final Level difficulty);
    abstract public void endStandard(final Player[] players);
    abstract public void endDuel(final String winner, final String loser);
    abstract public void exit();
}
