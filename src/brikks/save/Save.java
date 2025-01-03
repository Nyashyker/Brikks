package brikks.save;

import brikks.essentials.enums.*;
import brikks.essentials.*;
import brikks.save.container.*;

public interface Save {
    boolean playerExists(String name);
    PlayerSave[] save(final String[] name, final Level difficulty, final boolean duel);
    void save(final byte turn, final Position choice, final MatrixDice matrixDie);
    void updateDuration();
    void update(final byte turn, final Position choice, final MatrixDice matrixDie);
    PlayerLeaderboard[] leaderboard();
    SavedGame[] load();
    LoadedGame load(final int ID);
    void dropSave();
}
