package brikks.save;

import brikks.essentials.MatrixDice;
import brikks.essentials.Position;
import brikks.essentials.enums.Level;
import brikks.logic.Board;
import brikks.save.container.LoadedGame;
import brikks.save.container.PlayerLeaderboard;
import brikks.save.container.SavedGame;

import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseSave implements Save {
    private final DatabaseConnection dbc;
    /*TODO: assign in start-save or load*/
    private int ID;


    public DatabaseSave(final DatabaseConnection dbc) {
        this.dbc = dbc;
    }


    ///        Recreate the database
    private void save(final byte tableWidth, final byte tableHeight) throws SQLException {
        for (byte y = 0; y < tableHeight; y++) {
            for (byte x = 0; x < tableWidth; x++) {
                this.dbc.executeUpdate(String.format("INSERT INTO blocks (table_column, table_row) VALUES (%d, %d);",
                        x, y));
            }
        }
        this.dbc.executeUpdate("INSERT INTO blocks (table_column, table_row) VALUES (NULL, NULL);");
    }

    public void dropDB() throws SQLException {
        this.dbc.executeUpdate("""
                DROP TABLE IF EXISTS players_games;
                DROP TABLE IF EXISTS saved_boards;
                DROP TABLE IF EXISTS blocks;
                DROP TABLE IF EXISTS saved_players_games;
                DROP TABLE IF EXISTS players;
                DROP TABLE IF EXISTS games;
                DROP TABLE IF EXISTS saved_games;
                """);
    }

    public void recreateDB(final byte tableWidth, final byte tableHeight, final byte maxPlayerCount, final byte difficultyCount) throws SQLException {
        // Creating all the tables
        {
            this.dbc.executeUpdate(
                    String.format("""
                                    CREATE TABLE IF NOT EXISTS saved_games
                                    (
                                        save_id     SERIAL
                                            CONSTRAINT pk_save_save_id PRIMARY KEY,
                                        turn        SMALLINT
                                            CONSTRAINT nn_turn NOT NULL
                                            CONSTRAINT ch_turn CHECK ( turn >= 0 AND turn < %d ),
                                        roll_column SMALLINT
                                            CONSTRAINT nn_roll_column NOT NULL
                                            CONSTRAINT ch_roll_column CHECK ( roll_column >= 0 AND roll_column < %d ),
                                        roll_row    SMALLINT
                                            CONSTRAINT nn_roll_row NOT NULL
                                            CONSTRAINT ch_roll_row CHECK ( roll_row >= 0 AND roll_row < %d ),
                                        die_column  SMALLINT
                                            CONSTRAINT nn_die_column NOT NULL
                                            CONSTRAINT ch_die_column CHECK ( die_column >= 0 AND die_column < %d ),
                                        die_row     SMALLINT
                                            CONSTRAINT nn_die_row NOT NULL
                                            CONSTRAINT ch_die_row CHECK ( die_row >= 0 AND die_row < %d )
                                    );
                                    """, maxPlayerCount, tableWidth, tableHeight, tableWidth, tableHeight) +
                        String.format("""
                            
                                    CREATE TABLE IF NOT EXISTS games
                                    (
                                        game_id    SERIAL
                                            CONSTRAINT pk_game_id PRIMARY KEY,
                                        start_dt   TIMESTAMP
                                            CONSTRAINT nn_start NOT NULL,
                                        end_dt     TIMESTAMP,
                                        difficulty SMALLINT
                                            CONSTRAINT nn_difficulty NOT NULL
                                            CONSTRAINT ch_difficulty CHECK ( difficulty >= 0 AND difficulty < %d ),
                                        duel       BOOLEAN
                                            CONSTRAINT nn_duel NOT NULL,
                                        save_id    INT
                                            CONSTRAINT fk_save_game_id REFERENCES saved_games (save_id)
                                                ON DELETE SET NULL
                                    );
                                    """, difficultyCount) +
                            String.format("""
                            
                                    CREATE TABLE IF NOT EXISTS players
                                    (
                                        player_id SERIAL
                                            CONSTRAINT pk_player_id PRIMARY KEY,
                                        name      VARCHAR(%d)
                                            CONSTRAINT nn_name NOT NULL
                                    );
                                    """ +
                            String.format("""
                            
                                    CREATE TABLE IF NOT EXISTS saved_players_games
                                    (
                                        save_id      INT
                                            CONSTRAINT pk_save_player_id PRIMARY KEY,
                                        plays        BOOLEAN
                                            CONSTRAINT nn_plays NOT NULL,
                                        bombs        SMALLINT
                                            CONSTRAINT nn_bombs NOT NULL
                                            CONSTRAINT ch_bombs CHECK ( bombs >= 0 AND bombs < 4 ),
                                        energy       SMALLINT
                                            CONSTRAINT nn_energy NOT NULL
                                            CONSTRAINT ch_energy CHECK ( energy >= 0 AND energy < 28 ),
                                        energy_left  SMALLINT
                                            CONSTRAINT nn_energy_left NOT NULL
                                            CONSTRAINT ch_energy_left CHECK ( energy_left >= 0 AND energy_left <= 28 ),
                                        bonus_score  SMALLINT
                                            CONSTRAINT nn_bonus_score NOT NULL
                                            CONSTRAINT ch_bonus_score CHECK ( bonus_score >= 0 AND bonus_score < 15 ),
                                        player_order SMALLINT
                                            CONSTRAINT nn_player_order NOT NULL
                                            CONSTRAINT ch_player_order CHECK ( player_order >= 0 AND player_order < 4 )
                                    );
                            
                                    CREATE TABLE IF NOT EXISTS blocks
                                    (
                                        block        SMALLSERIAL
                                            CONSTRAINT pk_block PRIMARY KEY
                                            CONSTRAINT ch_block CHECK ( block >= 0 AND block < 25 ),
                                        table_column SMALLINT
                                            CONSTRAINT ch_table_column CHECK ( table_column >= 0 AND table_column < 4 ),
                                        table_row    SMALLINT
                                            CONSTRAINT ch_table_row CHECK ( table_row >= 0 AND table_row < 6 )
                                    );
                            
                                    CREATE TABLE IF NOT EXISTS saved_boards
                                    (
                                        save_id      INT
                                            CONSTRAINT fk_save_id REFERENCES saved_players_games (save_id)
                                                ON DELETE CASCADE,
                                        x            SMALLINT
                                            CONSTRAINT ch_x CHECK ( x >= 0 AND x < 4 ),
                                        y            SMALLINT
                                            CONSTRAINT ch_y CHECK ( y >= 0 AND y < 6 ),
                                        energy_bonus SMALLINT
                                            CONSTRAINT nn_energy_bonus NOT NULL
                                            CONSTRAINT ch_energy_bonus CHECK ( energy_bonus >= 0 AND energy_bonus < 6 ),
                                        block        SMALLINT
                                            CONSTRAINT fk_block REFERENCES blocks (block)
                                                ON DELETE RESTRICT
                                            CONSTRAINT ch_block CHECK ( block >= 0 AND block < 25 ),
                                        CONSTRAINT pk_save_cell PRIMARY KEY (save_id, x, y)
                                    );
                            
                                    CREATE TABLE IF NOT EXISTS players_games
                                    (
                                        game_id   INT
                                            CONSTRAINT fk_game_id REFERENCES games (game_id)
                                                ON DELETE RESTRICT,
                                        player_id INT
                                            CONSTRAINT fk_player_id REFERENCES players (player_id)
                                                ON DELETE RESTRICT,
                                        save_id   INT
                                            CONSTRAINT fk_save_player_games_id REFERENCES saved_players_games (save_id)
                                                ON DELETE SET NULL,
                                        duration  INTERVAL,
                                        score     SMALLINT
                                            CONSTRAINT ch_score CHECK ( score >= 0 AND score <= 191 ),
                                        CONSTRAINT pk_player_game PRIMARY KEY (game_id, player_id)
                                    );
                            """)
            );
        }


        final ResultSet blocksTableEnd = this.dbc.executeQuery(String.format("SELECT block FROM blocks WHERE block=%d;",
                tableHeight * tableWidth + 1));
        if (!blocksTableEnd.next()) {
            this.save(tableWidth, tableHeight);
        }
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
