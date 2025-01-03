package brikks.save;

import brikks.essentials.enums.*;
import brikks.essentials.*;
import brikks.logic.*;
import brikks.save.container.*;


public class DatabaseSave implements Save {
    private final DatabaseConnection dbc;
    /*TODO: assign in start-save or load*/
    private int ID;


    public DatabaseSave(final DatabaseConnection dbc) {
        this.dbc = dbc;
    }


    ///        Recreate the database
    private void save(final byte tableWidth, final byte tableHeight) {
        // TODO: implement
    }

    public void recreateDB(final byte tableWidth, final byte tableHeight) {
        // TODO: implement
    }


    ///        First save
    // SAVED_BOARDS
    private void save(final int saveID, final Board board) {
        // TODO: implement
    }

    // SAVE_GAMES
    private void save(final int saveID, final byte turn, final Position choice, final MatrixDice matrixDie) {
        // TODO: implement
    }


    @Override
    public boolean playerExists(String name) {
        // TODO: implement
        return false;
    }

    /*TODO: call on start*/
    @Override
    public PlayerSave[] save(final String[] name, final Level difficulty, final boolean duel) {
        // TODO: implement
        return null;
    }


    ///        Update
    @Override
    public void update(final byte turn, final Position choice, final MatrixDice matrixDie) {
        // TODO: implement
    }


    ///        Load
    @Override
    public PlayerLeaderboard[] leaderboard() {
        // TODO: implement
        return null;
    }

    @Override
    public SavedGame[] load() {
        // TODO: implement
        return null;
    }

    @Override
    public LoadedGame load(final int ID) {
        // TODO: implement
        return null;
    }


    ///        End game
    @Override
    public void dropSave() {
        // TODO: implement
    }
}
