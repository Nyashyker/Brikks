package brikks.save;

import brikks.Player;
import brikks.essentials.enums.Level;
import brikks.essentials.enums.Mode;

public abstract class Save {
    abstract public boolean playerExists(String name);


    abstract public SavedGame[] savedGames();

    abstract public LoadedGame loadGame(final int id);

    abstract public PlayerLiderboard[] liderboard();

    abstract public PlayerSave getPlayerSave(final String name);

    abstract public Rank[] getRanks();


    abstract public void save(final Level difficulty);

    abstract public void save(final Mode mode);

    abstract public void save(final Player player);


    abstract public void saveStartDateTime();

    abstract public void saveEndDateTime();

    abstract public void updateDuration(final int seconds);


    abstract public void dropSave();
}
