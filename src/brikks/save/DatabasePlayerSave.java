package brikks.save;


import brikks.Player;
import brikks.logic.*;


public class DatabasePlayerSave implements PlayerSave {
    private final DatabaseConnection dbc;
    private final int ID;


    public DatabasePlayerSave(final DatabaseConnection dbc, final int ID) {
        this.dbc = dbc;
        this.ID = ID;
    }


    ///		First save
    @Override
    // SAVED_PLAYERS_GAMES
    public void save(final Player player, final byte playerOrder) {
        // TODO: implement
    }

    ///		Update
    @Override
    public void update(final Player player) {
        // TODO: implement
    }

    ///		End game
    @Override
    public void save(final short score) {
        // TODO: implement
    }
}
