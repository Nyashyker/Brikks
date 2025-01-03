package brikks.save;

import brikks.essentials.enums.*;
import brikks.essentials.*;
import brikks.save.container.*;

public class EmptySave implements Save {
    public EmptySave() {}

    @Override
    public boolean playerExists(String name) { return false; }
    @Override
    public PlayerSave[] save(final String[] name, final Level difficulty, final boolean duel) {
        return new PlayerSave[0];
    }
    @Override
    public void update(final byte turn, final Position choice, final MatrixDice matrixDie) {}
    @Override
    public PlayerLeaderboard[] leaderboard() { return new PlayerLeaderboard[0]; }
    @Override
    public SavedGame[] load() { return new SavedGame[0]; }
    @Override
    public LoadedGame load(final int ID) { return null; }
    @Override
    public void dropSave() {}
}
