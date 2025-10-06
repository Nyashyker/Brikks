package brikks.save;

import brikks.BlocksTable;
import brikks.Brikks;
import brikks.Player;
import brikks.essentials.Position;
import brikks.essentials.enums.Level;
import brikks.save.container.LoadedGame;
import brikks.save.container.PlayerLeaderboard;
import brikks.save.container.SavedGame;
import brikks.view.container.LeaderboardOptions;

import java.util.ArrayList;
import java.util.List;

public class EmptySave extends Save {
    public EmptySave() {
        super(null);
    }


    ///        First save
    @Override
    public boolean playerExists(final String name) {
        return false;
    }

    @Override
    public void save(final String[] names, final Level difficulty, final boolean duel) {
    }

    ///        Update
    @Override
    public void save(final BlocksTable blocksTable, final Player[] players, final byte turn, final byte turnRotation, final Position choice, final Position matrixDie) {
    }

    @Override
    public void setDuration() {
    }

    @Override
    public void updateDuration(final byte turn) {
    }

    ///        Load
    @Override
    public List<PlayerLeaderboard> leaderboard(final LeaderboardOptions configurations) {
        return new ArrayList<>(0);
    }

    @Override
    public List<SavedGame> load() {
        return new ArrayList<>(0);
    }

    @Override
    public LoadedGame load(final int ID, final BlocksTable blocksTable) {
        final Player[] players = new Player[Brikks.MAX_PLAYERS];
        for (byte i = 0; i < players.length; i++) {
            players[i] = new Player("-EMPTY-" + (i + 1), Brikks.MAX_PLAYERS, Level.TWO);
        }
        return new LoadedGame(players, new Position(), (byte) 0, (byte) 0, new Position(), Level.TWO, false);
    }

    ///        End game
    @Override
    public void save(final byte order, final short score) {
    }

    @Override
    public void dropSave() {
    }
}
