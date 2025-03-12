package brikks.save;


import brikks.BlocksTable;
import brikks.Player;
import brikks.essentials.PlacedBlock;
import brikks.essentials.Position;
import brikks.essentials.enums.Color;
import brikks.logic.board.Board;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
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
    private void saveBoard(final int saveID, final Board board, final BlocksTable blocksTable) throws SQLException {
        final StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO saved_boards (save_id, x, y, energy_bonus, block) VALUES ");

        for (PlacedBlock placedBlock : board.getBoard()) {
            final Color energyBonus = board.getEnergyBonus()[placedBlock.getPlace().getY()][placedBlock.getPlace().getX()];

            final Position blockOrigin = blocksTable.findOrigin(placedBlock);
            final ResultSet blockID = this.dbc.executeQuery(String.format(
                    "SELECT block FROM blocks WHERE table_column=%d AND table_row=%d;",
                    blockOrigin.getX(), blockOrigin.getY()
            ));
            blockID.next();

            sqlBuilder.append(String.format(
                    "(%d, %d, %d, %s, %d),",
                    saveID,
                    placedBlock.getPlace().getX(),
                    placedBlock.getPlace().getY(),
                    energyBonus == null ? "NULL" : String.format("%d", energyBonus.ordinal() + 1),
                    blockID.getByte("block")
            ));
        }

        // Remove the trailing comma and finalize the SQL statement
        final String sql = sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).append(";").toString();
        this.dbc.executeUpdate(sql);
    }


    @Override
    // SAVED_PLAYERS_GAMES
    public void save(final BlocksTable blocksTable, final Player player, final byte playerOrder) {
        if (this.fail) {
            this.backup.save(blocksTable, player, playerOrder);
            return;
        }

        final String sql = String.format(
                """
                INSERT INTO saved_players_games (save_id, plays, bombs, energy, energy_left, bonus_score, player_order)
                VALUES (%d, %s, %d, %d, %d, %d, %d);
                """,
                this.ID,
                player.isPlays() ? "TRUE" : "FALSE",
                player.getBombs().get(),
                player.getEnergy().getPosition(),
                player.getEnergy().getAvailable(),
                player.getBonusScore().getScale(),
                playerOrder
        );

        try {
            this.dbc.executeUpdate(sql);
            this.saveBoard(this.ID, player.getBoard(), blocksTable);
        } catch (final SQLException _e) {
            System.out.println("Players first save has failed");
            System.out.println(_e.getMessage());
            this.fail = true;
            this.backup.save(blocksTable, player, playerOrder);
        }
    }


    ///        Update
    @Override
    public void setDuration() {
        if (this.fail) {
            this.backup.setDuration();
            return;
        }

        this.lastDurationUpdate = LocalTime.now();
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
                Duration.between(this.lastDurationUpdate, LocalTime.now()).getSeconds(),
                this.ID
        );

        try {
            this.dbc.executeUpdate(sql);
        } catch (final SQLException _e) {
            System.out.println("Saving duration has failed");
            System.out.println(_e.getMessage());
            this.fail = true;
            this.backup.updateDuration();
        }
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
        } catch (final SQLException _e) {
            System.out.println("Player save has failed");
            System.out.println(_e.getMessage());
            this.fail = true;
            this.backup.update(player);
        }
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
        } catch (final SQLException _e) {
            System.out.println("Final player save has failed");
            System.out.println(_e.getMessage());
            this.fail = true;
            this.backup.save(score);
        }
    }
}
