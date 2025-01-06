package brikks.save;

import brikks.essentials.Position;
import brikks.essentials.enums.Level;
import brikks.save.container.LoadedGame;
import brikks.save.container.PlayerLeaderboard;
import brikks.save.container.SavedGame;
import brikks.view.View;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class DatabaseSave extends Save {
    private final DatabaseConnection dbc;
    /*TODO: assign in start-save or load*/
    private int ID;


    public DatabaseSave(final DatabaseConnection dbc, final Save backupSaver) {
        super(backupSaver);
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

    public void recreateDB(
            final byte tableWidth,
            final byte tableHeight,
            final byte maxPlayers,
            final byte difficultyCount,
            final byte maxNameLen,
            final byte maxBombs,
            final byte maxEnergy,
            final byte maxBonusScore,
            final byte maxEnergyBonus,
            final short maxPossibleScore
    ) throws SQLException {
        this.dbc.executeUpdate(String.format("""
                CREATE TABLE IF NOT EXISTS saved_games
                (
                    save_id     INT
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
                """, maxPlayers, tableWidth, tableHeight, tableWidth, tableHeight));
        this.dbc.executeUpdate(String.format("""
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
                """, difficultyCount));
        this.dbc.executeUpdate(String.format("""
                CREATE TABLE IF NOT EXISTS players
                (
                    player_id SERIAL
                        CONSTRAINT pk_player_id PRIMARY KEY,
                    name      VARCHAR(%d)
                        CONSTRAINT nn_name NOT NULL
                );
                """, maxNameLen));
        this.dbc.executeUpdate(String.format("""
                CREATE TABLE IF NOT EXISTS saved_players_games
                (
                    save_id      SERIAL
                        CONSTRAINT pk_save_player_id PRIMARY KEY,
                    plays        BOOLEAN
                        CONSTRAINT nn_plays NOT NULL,
                    bombs        SMALLINT
                        CONSTRAINT nn_bombs NOT NULL
                        CONSTRAINT ch_bombs CHECK ( bombs >= 0 AND bombs < %d ),
                    energy       SMALLINT
                        CONSTRAINT nn_energy NOT NULL
                        CONSTRAINT ch_energy CHECK ( energy >= 0 AND energy < %d ),
                    energy_left  SMALLINT
                        CONSTRAINT nn_energy_left NOT NULL
                        CONSTRAINT ch_energy_left CHECK ( energy_left >= 0 AND energy_left <= %d ),
                    bonus_score  SMALLINT
                        CONSTRAINT nn_bonus_score NOT NULL
                        CONSTRAINT ch_bonus_score CHECK ( bonus_score >= 0 AND bonus_score < %d ),
                    player_order SMALLINT
                        CONSTRAINT nn_player_order NOT NULL
                        CONSTRAINT ch_player_order CHECK ( player_order >= 0 AND player_order < %d )
                );
                """, maxBombs, maxEnergy, maxEnergy, maxBonusScore, maxPlayers));
        this.dbc.executeUpdate(String.format("""
                CREATE TABLE IF NOT EXISTS blocks
                (
                    block        SMALLSERIAL
                        CONSTRAINT pk_block PRIMARY KEY
                        CONSTRAINT ch_block CHECK ( block > 0 AND block <= %d ),
                    table_column SMALLINT
                        CONSTRAINT ch_table_column CHECK ( table_column >= 0 AND table_column < %d ),
                    table_row    SMALLINT
                        CONSTRAINT ch_table_row CHECK ( table_row >= 0 AND table_row < %d )
                );
                """, tableWidth * tableHeight + 1, tableWidth, tableHeight));
        this.dbc.executeUpdate(String.format("""
                CREATE TABLE IF NOT EXISTS saved_boards
                (
                    save_id      INT
                        CONSTRAINT fk_save_id REFERENCES saved_players_games (save_id)
                            ON DELETE CASCADE,
                    x            SMALLINT
                        CONSTRAINT ch_x CHECK ( x >= 0 AND x < %d ),
                    y            SMALLINT
                        CONSTRAINT ch_y CHECK ( y >= 0 AND y < %d ),
                    energy_bonus SMALLINT
                        CONSTRAINT nn_energy_bonus NOT NULL
                        CONSTRAINT ch_energy_bonus CHECK ( energy_bonus >= 0 AND energy_bonus < %d ),
                    block        SMALLINT
                        CONSTRAINT fk_block REFERENCES blocks (block)
                            ON DELETE RESTRICT
                        CONSTRAINT ch_block CHECK ( block >= 0 AND block < %d ),
                    CONSTRAINT pk_save_cell PRIMARY KEY (save_id, x, y)
                );
                """, tableWidth, tableHeight, maxEnergyBonus, tableWidth * tableHeight + 1));
        this.dbc.executeUpdate(String.format("""
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
                                CONSTRAINT ch_score CHECK ( score >= 0 AND score <= %d ),
                            CONSTRAINT pk_player_game PRIMARY KEY (game_id, player_id)
                        );
                """, maxPossibleScore));


        final ResultSet blocksTableEnd = this.dbc.executeQuery(String.format("SELECT * FROM blocks WHERE block>=%d;",
                tableWidth * tableHeight + 1));
        if (!blocksTableEnd.next()) {
            final ResultSet blocksTableStart = this.dbc.executeQuery("SELECT * FROM blocks WHERE block=0;");
            if (blocksTableStart.next()) {
                throw new SQLException("Something weird is going on with blocks table. It is too short");
            }
            this.save(tableWidth, tableHeight);
        } else if (blocksTableEnd.next()) {
            throw new SQLException("Something weird is going on with blocks table. It is too long");
        }
    }


    ///        First save
    @Override
    public boolean playerExists(final String name) {
        if (this.fail) {
            return this.backup.playerExists(name);
        }

        try {
            final ResultSet player = this.dbc.executeQuery(String.format("SELECT * FROM players WHERE name='%s';",
                    name));
            return player.next();
        } catch (SQLException _e) {
            this.fail = true;
            return this.backup.playerExists(name);
        }
    }

    private int getGeneratedID(final String sql) throws SQLException {
        this.dbc.executeUpdate(sql);
        final ResultSet getID = this.dbc.getGeneratedKeys();
        if (getID.next()) {
            return getID.getInt(1);
        } else {
            throw new SQLException("ID did not generate");
        }
    }

    @Override
    public PlayerSave[] save(final String[] names, final Level difficulty, final boolean duel) {
        if (this.fail) {
            return this.backup.save(names, difficulty, duel);
        }

        final PlayerSave[] saves = new PlayerSave[names.length];
        try {
            this.ID = this.getGeneratedID(String.format("""
                    INSERT INTO games (start_dt, end_dt, difficulty, duel)
                    VALUES (NOW(), NULL, %d, %s);
                    """, difficulty.ordinal(), duel ? "TRUE" : "FALSE"));

            for (byte i = 0; i < saves.length; i++) {
                final int ID;
                if (this.playerExists(names[i])) {
                    // TODO: do something about problematic symbols in the name
                    final ResultSet getID = this.dbc.executeQuery(
                            String.format("SELECT player_id FROM players WHERE name='%s';", names[i])
                    );
                    if (getID.next()) {
                        ID = getID.getInt(1);
                    } else {
                        throw new SQLException("No ID found for existing player");
                    }
                } else {
                    // TODO: do something about problematic symbols in the name
                    ID = this.getGeneratedID(
                            String.format("INSERT INTO players (name) VALUES ('%s');", names[i])
                    );
                }

                saves[i] = new DatabasePlayerSave(this.dbc, ID);
            }

        } catch (SQLException _e) {
            this.fail = true;
            return this.backup.save(names, difficulty, duel);
        }

        return saves;
    }

    @Override
    public void save(final byte turn, final Position choice, final Position matrixDie) {
        if (this.fail) {
            this.backup.save(turn, choice, matrixDie);
            return;
        }

        try {
            this.dbc.executeUpdate(String.format("""
                    INSERT INTO saved_games (save_id, turn, roll_column, roll_row, die_column, die_row)
                    VALUES (%d, %d, %d, %d, %d, %d);
                    UPDATE games SET save_id=%d WHERE game_id=%d;
                    """, this.ID, turn, choice.getX(), choice.getY(), matrixDie.getX(), matrixDie.getY(), this.ID, this.ID));
        } catch (SQLException _e) {
            this.fail = true;
            this.backup.save(turn, choice, matrixDie);
        }
    }


    ///        Update
    @Override
    public void update(final byte turn, final Position choice, final Position matrixDie) {
        if (this.fail) {
            this.backup.update(turn, choice, matrixDie);
            return;
        }

        try {
            this.dbc.executeUpdate(String.format("""
                    UPDATE saved_games
                    SET turn=%d, roll_column=%d, roll_row=%d, die_column=%d, die_row=%d
                    WHERE save_id=%d;
                    """, turn, choice.getX(), choice.getY(), matrixDie.getX(), matrixDie.getY(), this.ID));
        } catch (SQLException _e) {
            this.fail = true;
            this.backup.update(turn, choice, matrixDie);
        }
    }


    ///        Load
    @Override
    public List<PlayerLeaderboard> leaderboard() {
        if (this.fail) {
            return this.backup.leaderboard();
        }

        final List<PlayerLeaderboard> leaderboard = new ArrayList<>(View.LEADERBOARD_COUNT);
        try {
            final ResultSet board = this.dbc.executeQuery(String.format("""
                    SELECT p.name, g.start_dt, g.end_dt, (TO_DATE('1970-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') + pg.duration) AS "duration", pg.score
                    FROM players_games pg
                    INNER JOIN games g on g.game_id = pg.game_id
                    INNER JOIN players p on p.player_id = pg.player_id
                    WHERE g.duel IS FALSE
                    ORDER BY pg.score DESC
                    LIMIT %d;
                    """, View.LEADERBOARD_COUNT));

            for (byte i = 0; board.next(); i++) {
                final String name = board.getString("name");
                final LocalDateTime startDT = board.getTimestamp("start_dt").toLocalDateTime();
                final LocalDateTime endDT = board.getTimestamp("end_dt").toLocalDateTime();

                final LocalDateTime duration = board.getTimestamp("duration").toLocalDateTime();

                final short score = board.getShort("score");

                leaderboard.add(new PlayerLeaderboard(name, startDT, endDT, duration, score));
            }
        } catch (SQLException _e) {
            this.fail = true;
            return this.backup.leaderboard();
        }

        return leaderboard;
    }

    @Override
    public List<SavedGame> load() {
        if (this.fail) {
            return this.backup.load();
        }

        final List<SavedGame> saved = new ArrayList<>();
        try {
            final ResultSet variants = this.dbc.executeQuery(
                    "SELECT g.game_id, g.start_dt FROM games g WHERE g.end_dt IS NULL ORDER BY g.start_dt DESC;"
            );
            while (variants.next()) {
                final int ID = variants.getInt("game_id");
                final ResultSet variant = this.dbc.executeQuery(
                        String.format("""
                                SELECT p.name
                                FROM saved_players_games spg
                                INNER JOIN players_games pg ON spg.save_id = pg.save_id
                                INNER JOIN players p ON p.player_id = pg.player_id
                                WHERE pg.game_id=%d
                                ORDER BY spg.player_order ASC;
                                """, ID)
                );

                final List<String> names = new ArrayList<>(4);
                while (variant.next()) {
                    names.add(variant.getString("name"));
                }

                final LocalDateTime start = variants.getTimestamp("start_dt").toLocalDateTime();

                saved.add(new SavedGame(ID, names, start));
            }
        } catch (SQLException _e) {
            this.fail = true;
            return this.backup.load();
        }

        return saved;
    }

    @Override
    public LoadedGame load(final int ID) {
        if (this.fail) {
            return this.backup.load(ID);
        }
        // TODO: implement
        return null;
    }


    ///        End game
    @Override
    public void dropSave() {
        if (this.fail) {
            this.backup.dropSave();
            return;
        }

        try {
            this.dbc.executeUpdate(
                    String.format("""
                            DELETE FROM saved_games WHERE save_id=%d;
                            DELETE FROM saved_players_games WHERE save_id IN
                            (SELECT save_id FROM players_games WHERE game_id=%d);
                            """, this.ID, this.ID)
            );
            // Respective parts of SAVED_BOARDS are deleted automatically with SAVED_PLAYERS_GAMES
        } catch (SQLException _e) {
            this.fail = true;
            this.backup.dropSave();
        }
    }
}
