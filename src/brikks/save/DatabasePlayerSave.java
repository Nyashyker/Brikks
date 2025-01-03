package brikks.save;

import brikks.Player;
import brikks.essentials.MatrixDice;
import brikks.essentials.Position;
import brikks.logic.Board;
import brikks.logic.Bombs;
import brikks.logic.BonusScore;
import brikks.logic.Energy;

public class DatabasePlayerSave extends PlayerSave {
    private final DatabaseConnection connection;
    private final int gameID;
    private int playerID;


    public DatabasePlayerSave(final DatabaseConnection connection, final int gameID, final int playerID) {
        this.connection = connection;
        this.gameID = gameID;
        this.playerID = playerID;
    }


    @Override
    public void save(final Board board) {
    }

    @Override
    public void save(final Bombs bombs) {

    }

    @Override
    public void save(final BonusScore bonusScore) {

    }

    @Override
    public void save(final Energy energy) {

    }

    @Override
    public void save(final short finalScore) {

    }

    @Override
    public void save(final MatrixDice matrixDie) {

    }

    @Override
    public void save(final Player player) {

    }

    @Override
    public void save(final Position matrixDie) {

    }
}
