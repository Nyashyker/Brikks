package brikks.save;


import brikks.Player;
import brikks.essentials.PlacedBlock;
import brikks.logic.Board;

import java.sql.SQLException;
import java.time.LocalTime;


public class DatabasePlayerSave extends PlayerSave {
    private final DatabaseConnection dbc;
    private final int ID;
    private LocalTime lastDurationUpdate;


    public DatabasePlayerSave(final DatabaseConnection dbc, final int ID, final PlayerSave backup) {
        super(backup);
        this.dbc = dbc;
        this.ID = ID;
    }


    ///        First save
    // SAVED_BOARDS
    private void saveBoard(final int saveID, final Board board) throws SQLException {
        final StringBuilder sqlBuilder = new StringBuilder();
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
        final String sql = sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).append(";").toString();
        this.dbc.executeUpdate(sql);
    }


    @Override
    // SAVED_PLAYERS_GAMES
    public void save(final Player player, final byte playerOrder) {
        if (this.fail) {
            this.backup.save(player, playerOrder);
            return;
        }

        final String sql = String.format(
                "INSERT INTO saved_players_games (save_id, bombs, energy, energy_left, bonus_score, player_order) " +
                        "VALUES (%d, %d, %d, %d, %d, %d);",
                this.ID,
                player.getBombs().get(),
                player.getEnergy().getPosition(),
                player.getEnergy().getAvailable(),
                player.getBonusScore().getScale(),
                playerOrder
        );

        try {
            this.dbc.executeUpdate(sql);
            saveBoard(this.ID, player.getBoard());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.fail = true;
            this.backup.save(player, playerOrder);
        }
        System.out.println("Gravc'a zbereglo");
    }


    ///        Update
    @Override
    public void setDuration() {
        if (this.fail) {
            this.backup.setDuration();
            return;
        }

        this.lastDurationUpdate = LocalTime.now();
        System.out.println("Tajmer ustanovleno");
    }

    @Override
    public void updateDuration() {
        if (this.fail) {
            this.backup.updateDuration();
            return;
        }

        if (this.lastDurationUpdate == null) { return; }

        final String sql = String.format(
                "UPDATE players_games SET duration = duration + interval '%d seconds' WHERE save_id = %d;",
                java.time.Duration.between(this.lastDurationUpdate, LocalTime.now()).getSeconds(),
                this.ID
        );

        try {
            this.dbc.executeUpdate(sql);
            System.out.println("Promizxok zberezxeno");
        } catch (SQLException e) {
            System.out.println("Pomylocxka: zberegty tryvalist'");
            System.out.println(e.getMessage());
            this.fail = true;
            this.backup.updateDuration();
        }
        System.out.println("Tajmer skynuto");
    }

    @Override
    public void update(final Player player) {
        if (this.fail) {
            this.backup.update(player);
            return;
        }

        final String sql = String.format(
                "UPDATE saved_players_games SET bombs = %d, energy = %d, energy_left = %d, bonus_score = %d WHERE save_id = %d;",
                player.getBombs().get(),
                player.getEnergy().getPosition(),
                player.getEnergy().getAvailable(),
                player.getBonusScore().getScale(),
                this.ID
        );

        try {
            this.dbc.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Pomylocxka v onovlenni gravc'a");
            System.out.println(e.getMessage());
            this.fail = true;
            this.backup.update(player);
        }
        System.out.println("Gravc'a onovyly");
    }


    ///        End game
    @Override
    public void save(final short score) {
        if (this.fail) {
            this.backup.save(score);
            return;
        }

        final String sql = String.format(
                "UPDATE players_games SET score = %d WHERE save_id = %d;",
                score,
                this.ID
        );

        try {
            this.dbc.executeUpdate(sql);
        } catch (SQLException e) {
            this.fail = true;
            this.backup.save(score);
        }
    }
}
