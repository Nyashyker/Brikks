package brikks.save;

import brikks.BlocksTable;
import brikks.essentials.Position;
import brikks.essentials.enums.Level;
import brikks.save.container.LoadedGame;
import brikks.save.container.PlayerLeaderboard;
import brikks.save.container.SavedGame;

import java.util.ArrayList;
import java.util.List;

public class EmptySave extends Save {
    public EmptySave() {
        super(null);
    }

    @Override
    public boolean playerExists(final String name) {
        return false;
    }

    @Override
    public PlayerSave[] save(final String[] names, final Level difficulty, final boolean duel) {
        final PlayerSave[] saves = new PlayerSave[names.length];
        for (byte i = 0; i < saves.length; i++) {
            saves[i] = new EmptyPlayerSave();
        }

        return saves;
    }

    @Override
    public void save(final byte turn, final byte turnRotation, final Position choice, final Position matrixDie) {
    }

    @Override
    public void update(final byte turn, final byte turnRotation, final Position choice, final Position matrixDie) {
    }

    @Override
    public List<PlayerLeaderboard> leaderboard() {
        return new ArrayList<>();
    }

    @Override
    public List<SavedGame> load() {
        return new ArrayList<>();
    }

    @Override
    public LoadedGame load(final int ID, final BlocksTable blocksTable) {
        return null;
    }

    @Override
    public void dropSave() {
    }
}
