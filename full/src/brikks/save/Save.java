package brikks.save;

import brikks.Player;
import brikks.essentials.enums.*;
import brikks.save.container.*;

// TODO: make it keep track of time
public abstract class Save {
    abstract public boolean playerExists(String name);


    abstract public SavedGame[] savedGames();

    abstract public LoadedGame loadGame(final int id);

    abstract public PlayerLeaderboard[] leaderboard();

    abstract public PlayerSave getPlayerSave(final String name);


    abstract public void save(final Level difficulty);

    abstract public void save(final boolean duel);

    abstract public void save(final Player player);


    abstract public void saveStartDateTime();

    abstract public void saveEndDateTime();

    abstract public void startCountingTime();

    abstract public void updateDuration();
}
