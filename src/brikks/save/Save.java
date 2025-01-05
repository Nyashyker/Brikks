package brikks.save;

import brikks.essentials.enums.*;
import brikks.essentials.*;
import brikks.save.container.*;


public abstract class Save {
    protected final Save backup;
    protected boolean fail;


    protected Save(Save backup) {
        this.backup = backup;
        this.fail = false;
    }


    ///        First save
    abstract public boolean playerExists(final String name);
    abstract public PlayerSave[] save(final String[] names, final Level difficulty, final boolean duel);
    abstract public void save(final byte turn, final Position choice, final Position matrixDie);
    ///        Update
    abstract public void update(final byte turn, final Position choice, final Position matrixDie);
    ///        Load
    abstract public PlayerLeaderboard[] leaderboard();
    abstract public SavedGame[] load();
    abstract public LoadedGame load(final int ID);
    ///        End game
    abstract public void dropSave();
}
