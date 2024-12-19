package brikks.save;

import brikks.Player;
import brikks.essentials.enums.*;
import brikks.save.container.LoadedGame;
import brikks.save.container.PlayerLiderboard;
import brikks.save.container.Rank;
import brikks.save.container.SavedGame;

public abstract class Save {
    abstract public boolean playerExists(String name);


    abstract public SavedGame[] savedGames();

    abstract public LoadedGame loadGame(final int id);

    abstract public PlayerLiderboard[] liderboard();

    abstract public PlayerSave getPlayerSave(final String name);

    abstract public Rank[] getRanks();


    abstract public void save(final Level difficulty);

    abstract public void save(final boolean duel);

    abstract public void save(final Player player);


    abstract public void saveStartDateTime();

    abstract public void saveEndDateTime();

    abstract public void updateDuration();
}
