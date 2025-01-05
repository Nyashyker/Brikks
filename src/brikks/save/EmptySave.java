package brikks.save;

import brikks.essentials.enums.*;
import brikks.essentials.*;
import brikks.save.container.*;

public class EmptySave extends Save {
    public EmptySave() { super(null); }

    @Override
    public boolean playerExists(final String name) { return false; }
    @Override
    public PlayerSave[] save(final String[] names, final Level difficulty, final boolean duel) { return new PlayerSave[0]; }
    @Override
    public void save(final byte turn, final Position choice, final Position matrixDie) {}
    @Override
    public void update(final byte turn, final Position choice, final Position matrixDie) {}
    @Override
    public PlayerLeaderboard[] leaderboard() { return new PlayerLeaderboard[0]; }
    @Override
    public SavedGame[] load() { return new SavedGame[0]; }
    @Override
    public LoadedGame load(final int ID) { return null; }
    @Override
    public void dropSave() {}
}
