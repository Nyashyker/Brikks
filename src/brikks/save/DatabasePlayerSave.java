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
    private final int gameID;
    private final int playerID;
    private LocalTime lastDurationUpdate;

    private int saveID;


    public DatabasePlayerSave(final DatabaseConnection dbc, final int gameID, final int playerID, final PlayerSave backup) {
        this(dbc, gameID, playerID, -1, backup);
    }

    public DatabasePlayerSave(final DatabaseConnection dbc, final int gameID, final int playerID, final int saveID, final PlayerSave backup) {
        super(backup);
        this.dbc = dbc;
        this.gameID = gameID;
        this.playerID = playerID;

        this.saveID = saveID;
    }


    ///        First save
    // SAVED_BOARDS
    private void saveBoard(final Board board, final BlocksTable blocksTable) throws SQLException {
        if (this.saveID == -1) {
            throw new IllegalArgumentException("SaveID has not been generated yet, but `saveBoard` uses it");
        }

        this.dbc.executeUpdate(String.format("DELETE FROM saved_boards WHERE save_id = %d;", this.saveID));

        final Color[][] energyBonus = board.getEnergyBonus();

        // Placed Blocks
        for (final PlacedBlock placedBlock : board.getBoard()) {
            final int blockID;
            {
                if (blockOrigin == null) {
                    blockID = BlocksTable.HEIGHT * BlocksTable.WIDTH + 1;
                } else {
                    final ResultSet getterBlockID = this.dbc.executeQuery(String.format(
                            "SELECT block FROM blocks WHERE table_column=%d AND table_row=%d;",
                            blockOrigin.getX(), blockOrigin.getY()
                    ));
                    if (getterBlockID.next()) {
                        blockID = getterBlockID.getByte("block");
                    } else {
                        throw new SQLException("Block ID is not found");
                    }
                }
            }

            this.dbc.executeUpdate(String.format("""
                            INSERT INTO saved_boards (save_id, x, y, energy_bonus, block)
                            VALUES (%d, %d, %d, %d, %d);
                            """,
                    this.saveID,
                    placedBlock.getPlace().getX(),
                    placedBlock.getPlace().getY(),
                    placedBlock.getColor().ordinal() + 1,
                    blockID
            ));
        }

        // Energy Bonus
        for (byte y = 0; y < energyBonus.length; y++) {
            for (byte x = 0; x < energyBonus[0].length; x++) {
                final Color colorOfBonus = energyBonus[y][x];
                if (colorOfBonus != null) {
                    this.dbc.executeUpdate(String.format("""
                            INSERT INTO saved_boards (save_id, x, y, energy_bonus, block)
                            VALUES (%d, %d, %d, %d, NULL);
                            """,
                            this.saveID,
                            x,
                            y,
                            colorOfBonus.ordinal() + 1
                    ));
                }
            }
        }
    }


    @Override
    // SAVED_PLAYERS_GAMES
    public void save(final BlocksTable blocksTable, final Player player, final byte playerOrder) {
        if (this.fail) {
            this.backup.save(blocksTable, player, playerOrder);
            return;
        }

        try {
            this.saveID = DatabaseSave.getGeneratedID(this.dbc,
                    String.format("""
                                    INSERT INTO saved_players_games (plays, bombs, energy, energy_left, bonus_score, player_order)
                                    VALUES (%s, %d, %d, %d, %d, %d)
                                    """,
                            player.isPlays() ? "TRUE" : "FALSE",
                            player.getBombs().get(),
                            player.getEnergy().getPosition(),
                            player.getEnergy().getAvailable(),
                            player.getBonusScore().getScale(),
                            playerOrder
                    ), "save_id");

            this.dbc.executeUpdate(String.format(
                    "UPDATE players_games SET save_id = %d WHERE game_id = %d AND player_id = %d;",
                    this.saveID, this.gameID, this.playerID
            ));
            System.out.println("Player " + player.name + " id=" + this.playerID + ", game=" + this.gameID + ", save=" + this.saveID);

            this.saveBoard(player.getBoard(), blocksTable);
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

        if (this.lastDurationUpdate == null) {
            return;
        }

        final String sql = String.format(
                "UPDATE players_games SET duration = duration + interval '%d seconds' WHERE player_id = %d AND game_id = %d;",
                Duration.between(this.lastDurationUpdate, LocalTime.now()).getSeconds(),
                this.playerID,
                this.gameID
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
    public void update(final Player player, final BlocksTable blocksTable) {
        if (this.saveID == -1) {
            throw new IllegalArgumentException("SaveID has not been generated yet, but `update` uses it");
        }

        if (this.fail) {
            this.backup.update(player, blocksTable);
            return;
        }

        try {
            this.dbc.executeUpdate(String.format(
                    "UPDATE saved_players_games SET bombs = %d, energy = %d, energy_left = %d, bonus_score = %d WHERE save_id = %d;",
                    player.getBombs().get(),
                    player.getEnergy().getPosition(),
                    player.getEnergy().getAvailable(),
                    player.getBonusScore().getScale(),
                    this.saveID
            ));

            this.saveBoard(player.getBoard(), blocksTable);
        } catch (final SQLException _e) {
            System.out.println("Player save has failed");
            System.out.println(_e.getMessage());
            this.fail = true;
            this.backup.update(player, blocksTable);
        }
    }


    ///        End game
    @Override
    public void save(final short score) {
        if (this.fail) {
            this.backup.save(score);
            return;
        }

        try {
            this.dbc.executeUpdate(String.format(
                    "UPDATE players_games SET score = %d WHERE player_id = %d;",
                    score,
                    this.playerID
            ));
        } catch (final SQLException _e) {
            System.out.println("Final player save has failed");
            System.out.println(_e.getMessage());
            this.fail = true;
            this.backup.save(score);
        }
    }
}
