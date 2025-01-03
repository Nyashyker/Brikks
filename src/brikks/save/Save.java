package brikks.save;

import brikks.essentials.enums.*;
import brikks.essentials.*;
import brikks.save.container.*;


public interface Save {
    ///        First save
    boolean playerExists(String name);
    PlayerSave[] save(final String[] name, final Level difficulty, final boolean duel);
    ///        Update
    void update(final byte turn, final Position choice, final MatrixDice matrixDie);
    ///        Load
    PlayerLeaderboard[] leaderboard();
    SavedGame[] load();
    LoadedGame load(final int ID);
    ///        End game
    void dropSave();
}
