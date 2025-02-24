package brikks.save;

import brikks.BlocksTable;
import brikks.essentials.enums.*;
import brikks.essentials.*;
import brikks.save.container.*;

import java.util.List;


public abstract class Save {
    protected final Save backup;
    protected boolean fail;


    protected Save(final Save backup) {
        this.backup = backup;
        this.fail = false;
    }


    ///        First save
    abstract public boolean playerExists(final String name);
    abstract public PlayerSave[] save(final String[] names, final Level difficulty, final boolean duel);
    abstract public void save(final byte turn, final byte turnRotation, final Position choice, final Position matrixDie);
    ///        Update
    abstract public void update(final byte turn, final byte turnRotation, final Position choice, final Position matrixDie);
    ///        Load
    abstract public List<PlayerLeaderboard> leaderboard();
    abstract public List<SavedGame> load();
    abstract public LoadedGame load(final int ID, final BlocksTable blocksTable);
    ///        End game
    abstract public void dropSave();
}
