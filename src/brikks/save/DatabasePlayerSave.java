package brikks.save;


import brikks.Player;
import brikks.essentials.PlacedBlock;
import brikks.essentials.Position;
import brikks.logic.Board;
import brikks.logic.BonusScore;
import brikks.logic.Energy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private void saveBoard(final int saveID, final Board board) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO saved_boards (save_id, x, y, energy_bonus, block) VALUES ");

        for (PlacedBlock placedBlock : board.getBoard()) {
            sqlBuilder.append(String.format(
                    "(%d, %d, %d, %d, %d),",
                    saveID,
                    placedBlock.getPlace().getX(),
                    placedBlock.getPlace().getY(),
                    board.getEnergyBonus()[placedBlock.getPlace().getY()][placedBlock.getPlace().getX()].ordinal(),
                    placedBlock.hashCode()
            ));
        }

        // Remove the trailing comma and finalize the SQL statement
        String sql = sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).append(";").toString();
        try {
            dbc.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    @Override
    // SAVED_PLAYERS_GAMES
    public void save(final Player player, final byte playerOrder) {
        String sql = String.format(
                "INSERT INTO saved_players_games (save_id, bombs, energy, energy_left, bonus_score, player_order) " +
                        "VALUES (%d, %d, %d, %d, %d, %d);",
                ID,
                player.getBombs().get(),
                player.getEnergy().getPosition(),
                player.getEnergy().getAvailable(),
                player.getBonusScore().getScale(),
                playerOrder
        );

        try {
            dbc.executeUpdate(sql);
            saveBoard(ID, player.getBoard());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    ///        Update
    @Override
    public void setDuration() {
        this.lastDurationUpdate = LocalTime.now();

    }

    @Override
    public void updateDuration() {
        if (lastDurationUpdate == null) return;

        String sql = String.format(
                "UPDATE players_games SET duration = duration + interval '%d seconds' WHERE save_id = %d;",
                java.time.Duration.between(lastDurationUpdate, LocalTime.now()).getSeconds(),
                ID
        );

        try {
            dbc.executeUpdate(sql);
            setDuration(); // Reset the last update time
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(final Player player) {
        String sql = String.format(
                "UPDATE saved_players_games SET bombs = %d, energy = %d, energy_left = %d, bonus_score = %d WHERE save_id = %d;",
                player.getBombs().get(),
                player.getEnergy().getPosition(),
                player.getEnergy().getAvailable(),
                player.getBonusScore().getScale(),
                ID
        );

        try {
            dbc.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    ///        End game
    @Override
    public void save(final short score){
        String sql = String.format(
                "UPDATE players_games SET score = %d WHERE save_id = %d;",
                score,
                ID
        );

        try {
            dbc.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
