package brikks.save;


import brikks.Player;
import brikks.logic.Board;

import java.time.LocalTime;


public class DatabasePlayerSave implements PlayerSave {
    private final DatabaseConnection dbc;
    private final int ID;
    private LocalTime lastDurationUpdate;


    public DatabasePlayerSave(final DatabaseConnection dbc, final int ID) {
        this.dbc = dbc;
        this.ID = ID;
    }


    ///        First save
    // SAVED_BOARDS
    private void save(final int saveID, final Board board) {
        // TODO: implement
    }

    @Override
    // SAVED_PLAYERS_GAMES
    public void save(final Player player, final byte playerOrder) {
        // TODO: implement
    }


    ///        Update
    @Override
    public void setDuration() {
        this.lastDurationUpdate = LocalTime.now();
    }

    @Override
    public void updateDuration() {
        // TODO: implement
    }

    @Override
    public void update(final Player player) {
        // TODO: implement
    }


    ///        End game
    @Override
    public void save(final short score) {
        // TODO: implement
    }
}
