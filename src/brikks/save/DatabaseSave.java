package brikks.save;

import brikks.BlocksTable;
import brikks.Brikks;
import brikks.Player;
import brikks.essentials.PlacedBlock;
import brikks.essentials.Position;
import brikks.essentials.enums.Color;
import brikks.essentials.enums.Level;
import brikks.logic.Bombs;
import brikks.logic.BonusScore;
import brikks.logic.Energy;
import brikks.logic.board.Board;
import brikks.save.container.LoadedGame;
import brikks.save.container.PlayerLeaderboard;
import brikks.save.container.SavedGame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class DatabaseSave extends Save {
    private final DatabaseConnection dbc;
    // Per game
    private int gameID;
    private LocalTime lastDurationUpdate;
    private boolean firstSave;
    // Per player
    private int[] playerID;
    private int[] playerSaveID;


    public DatabaseSave(final DatabaseConnection dbc, final Save backup) {
        super(backup);
        this.dbc = dbc;

        this.gameID = -1;
        this.lastDurationUpdate = null;
        this.firstSave = true;

        this.playerID = null;
        this.playerSaveID = null;
    }


    ///        Recreate the database
    public void dropDB() throws SQLException {
        this.dbc.executeUpdate("""
                DROP TABLE IF EXISTS players_games;
                DROP TABLE IF EXISTS saved_boards;
                DROP TABLE IF EXISTS saved_players_games;
                DROP TABLE IF EXISTS players;
                DROP TABLE IF EXISTS saved_games;
                DROP TABLE IF EXISTS games;
                """
        );
    }

    public void recreateDB(
            final byte difficulties,
            final byte maxPlayers,
            final byte tableWidth,
            final byte tableHeight,
            final byte maxNameLen,
            final byte maxBombs,
            final byte maxEnergy,
            final byte maxBonusScore,
            final byte boardWidth,
            final byte boardHeight,
            final byte colors,
            final short maxPossibleScore
    ) throws SQLException {

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
                                        CONSTRAINT nn_duel NOT NULL
                                );
                                """,
                        difficulties
                )
        );

        this.dbc.executeUpdate(String.format("""
                                CREATE TABLE IF NOT EXISTS saved_games
                                (
                                    g_save_id     INT
                                        CONSTRAINT pk_g_save_id PRIMARY KEY
                                        CONSTRAINT fk_g_save_id REFERENCES games (game_id)
                                            ON DELETE RESTRICT,
                                    turn          SMALLINT
                                        CONSTRAINT nn_turn NOT NULL
                                        CONSTRAINT ch_turn CHECK ( turn >= 0 AND turn < %d ),
                                    turn_rotation SMALLINT
                                        CONSTRAINT nn_turn_rotation NOT NULL
                                        CONSTRAINT ch_turn_rotation CHECK ( turn >= 0 AND turn < %d ),
                                    roll_column   SMALLINT
                                        CONSTRAINT nn_roll_column NOT NULL
                                        CONSTRAINT ch_roll_column CHECK ( roll_column >= 0 AND roll_column < %d ),
                                    roll_row      SMALLINT
                                        CONSTRAINT nn_roll_row NOT NULL
                                        CONSTRAINT ch_roll_row CHECK ( roll_row >= 0 AND roll_row < %d ),
                                    die_column    SMALLINT
                                        CONSTRAINT nn_die_column NOT NULL
                                        CONSTRAINT ch_die_column CHECK ( die_column >= 0 AND die_column < %d ),
                                    die_row       SMALLINT
                                        CONSTRAINT nn_die_row NOT NULL
                                        CONSTRAINT ch_die_row CHECK ( die_row >= 0 AND die_row < %d )
                                );
                                """,
                        maxPlayers, maxPlayers, tableWidth, tableHeight, tableWidth, tableHeight
                )
        );

        this.dbc.executeUpdate(String.format("""
                                CREATE TABLE IF NOT EXISTS players
                                (
                                    player_id SERIAL
                                        CONSTRAINT pk_player_id PRIMARY KEY,
                                    name      VARCHAR(%d)
                                        CONSTRAINT nn_name NOT NULL
                                        CONSTRAINT uq_name UNIQUE
                                );
                                """,
                        maxNameLen
                )
        );

        this.dbc.executeUpdate(String.format("""
                                CREATE TABLE IF NOT EXISTS saved_players_games
                                (
                                    pg_save_id   SERIAL
                                        CONSTRAINT pk_player_game_save_player_id PRIMARY KEY,
                                    plays        BOOLEAN
                                        CONSTRAINT nn_plays NOT NULL,
                                    bombs        SMALLINT
                                        CONSTRAINT nn_bombs NOT NULL
                                        CONSTRAINT ch_bombs CHECK ( bombs >= 0 AND bombs <= %d ),
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
                                """,
                        maxBombs, maxEnergy, maxEnergy, maxBonusScore, maxPlayers
                )
        );

        this.dbc.executeUpdate(String.format("""
                                CREATE TABLE IF NOT EXISTS saved_boards
                                (
                                    pg_save_id INT
                                        CONSTRAINT fk_player_game_save_id REFERENCES saved_players_games (pg_save_id)
                                            ON DELETE CASCADE,
                                    x          SMALLINT
                                        CONSTRAINT ch_x CHECK ( x >= 0 AND x < %d ),
                                    y          SMALLINT
                                        CONSTRAINT ch_y CHECK ( y >= 0 AND y < %d ),
                                    color      SMALLINT
                                        CONSTRAINT nn_color NOT NULL
                                        CONSTRAINT ch_color CHECK ( color >= 0 AND color < %d ),
                                    block      SMALLINT
                                        CONSTRAINT ch_block CHECK ( block > 0 AND block <= %d ),
                                    CONSTRAINT pk_save_cell PRIMARY KEY (pg_save_id, x, y)
                                );
                                """,
                        boardWidth, boardHeight, colors, tableWidth * tableHeight + 1
                )
        );

        this.dbc.executeUpdate(String.format("""
                                CREATE TABLE IF NOT EXISTS players_games
                                (
                                    player_id  INT
                                        CONSTRAINT nn_player_id NOT NULL
                                        CONSTRAINT fk_player_id REFERENCES players (player_id)
                                            ON DELETE RESTRICT,
                                    game_id    INT
                                        CONSTRAINT nn_game_id NOT NULL
                                        CONSTRAINT fk_game_id REFERENCES games (game_id)
                                            ON DELETE RESTRICT,
                                    pg_save_id INT
                                        CONSTRAINT fk_player_game_save_id REFERENCES saved_players_games (pg_save_id)
                                            ON DELETE SET NULL,
                                    duration   INTERVAL,
                                    score      SMALLINT
                                        CONSTRAINT ch_score CHECK ( score >= 0 AND score <= %d ),
                                    CONSTRAINT pk_player_game PRIMARY KEY (player_id, game_id)
                                );
                                """,
                        maxPossibleScore
                )
        );
    }


    ///        First save
    @Override
    public boolean playerExists(final String name) {
        if (this.fail) {
            return this.backup.playerExists(name);
        }

        try {
            final ResultSet player = this.dbc.executeQuery(String.format("""
                                    SELECT *
                                    FROM players p
                                    WHERE p.name = %s
                                    LIMIT 1;
                                    """,
                            formatStr(name)
                    )
            );
            return player.next();

        } catch (final SQLException _e) {
            System.err.println("Checking player existence has failed");
            System.err.println(_e.getMessage());
            this.fail = true;
            return this.backup.playerExists(name);
        }
    }

    @Override
    public void save(final String[] names, final Level difficulty, final boolean duel) {
        if (this.fail) {
            this.backup.save(names, difficulty, duel);
            return;
        }

        try {
            {
                final ResultSet getGameID = this.dbc.executeQuery(String.format("""
                                        INSERT INTO games (start_dt, end_dt, difficulty, duel)
                                        VALUES (NOW(), NULL, %d, %s)
                                        RETURNING game_id AS "game_id";
                                        """,
                                difficulty.ordinal(), formatBool(duel)
                        )
                );
                if (getGameID.next()) {
                    this.gameID = getGameID.getInt("game_id");
                } else {
                    throw new SQLException("No ID for the game");
                }
            }

            this.playerID = new int[names.length];
            this.playerSaveID = null;
            for (byte order = 0; order < names.length; order++) {
                {
                    final String formatedName = formatStr(names[order]);
                    final ResultSet getPlayerID = this.dbc.executeQuery(
                            String.format("""
                                            WITH new_player_id AS (INSERT INTO players (name) VALUES (%s)
                                                ON CONFLICT (name) DO NOTHING RETURNING player_id AS "player_id")
                                            SELECT player_id AS "player_id"
                                            FROM new_player_id
                                            UNION ALL
                                            SELECT p.player_id AS "player_id"
                                            FROM players p
                                            WHERE p.name = %s
                                            LIMIT 1;
                                            """,
                                    formatedName,
                                    formatedName
                            )
                    );
                    if (getPlayerID.next()) {
                        this.playerID[order] = getPlayerID.getInt("player_id");
                    } else {
                        throw new SQLException("No ID for player");
                    }
                }

                this.dbc.executeUpdate(String.format("""
                                        INSERT INTO players_games (game_id, player_id, pg_save_id, duration, score)
                                        VALUES (%d, %d, NULL, interval '0.0', NULL);
                                        """,
                                this.gameID, this.playerID[order]
                        )
                );
            }

        } catch (final SQLException _e) {
            System.err.println("Global forced save has failed");
            System.err.println(_e.getMessage());
            this.fail = true;
        }
    }

    private void firstSave(final BlocksTable blocksTable, final Player[] players, final byte turn, final byte turnRotation, final Position choice, final Position matrixDie) {
        if (this.fail) {
            this.backup.save(blocksTable, players, turn, turnRotation, choice, matrixDie);
            return;
        }

        try {
            this.dbc.executeUpdate(String.format("""
                            INSERT INTO saved_games (g_save_id, turn, turn_rotation, roll_column, roll_row, die_column, die_row)
                            VALUES (%d, %d, %d, %d, %d, %d, %d);
                            """,
                    this.gameID,
                    turn,
                    turnRotation,
                    choice.getX(),
                    choice.getY(),
                    matrixDie.getX(),
                    matrixDie.getY()
            ));

            this.playerSaveID = new int[players.length];
            for (int order = 0; order < players.length; order++) {
                final ResultSet getPlayerSaveID = this.dbc.executeQuery(String.format("""
                                        INSERT INTO saved_players_games (plays, bombs, energy, energy_left, bonus_score, player_order)
                                        VALUES (%s, %d, %d, %d, %d, %d)
                                        RETURNING pg_save_id AS "pg_save_id";
                                        """,
                                formatBool(players[order].isPlays()),
                                players[order].getBombs().get(),
                                players[order].getEnergy().getPosition(),
                                players[order].getEnergy().getAvailable(),
                                players[order].getBonusScore().getScale(),
                                order
                        )
                );
                if (getPlayerSaveID.next()) {
                    this.playerSaveID[order] = getPlayerSaveID.getInt("pg_save_id");
                } else {
                    throw new SQLException("No save ID for player");
                }

                this.dbc.executeUpdate(String.format("""
                                        UPDATE players_games
                                        SET pg_save_id = %d
                                        WHERE game_id = %d
                                          AND player_id = %d;
                                        """,
                                this.playerSaveID[order],
                                this.gameID,
                                this.playerID[order]
                        )
                );

                this.resaveBoard(blocksTable, players[order].getBoard(), this.playerSaveID[order]);
            }

        } catch (final SQLException _e) {
            System.err.println("Global first save has failed");
            System.err.println(_e.getMessage());
            this.fail = true;
            this.backup.save(blocksTable, players, turn, turnRotation, choice, matrixDie);
        }
    }


    ///        Update
    @Override
    public void save(final BlocksTable blocksTable, final Player[] players, final byte turn, final byte turnRotation, final Position choice, final Position matrixDie) {
        if (this.firstSave) {
            this.firstSave(blocksTable, players, turn, turnRotation, choice, matrixDie);
            this.firstSave = false;
        } else {
            this.update(blocksTable, players, turn, turnRotation, choice, matrixDie);
        }
    }

    @Override
    public void setDuration() {
        if (this.fail) {
            this.backup.setDuration();
            return;
        }

        this.lastDurationUpdate = LocalTime.now();
    }

    @Override
    public void updateDuration(final byte turn) {
        if (this.fail) {
            this.backup.updateDuration(turn);
            return;
        }

        if (this.lastDurationUpdate == null) {
            return;
        }

        try {
            setProperIntervalStandard(this.dbc);
            this.dbc.executeUpdate(String.format("""
                                    UPDATE players_games
                                    SET duration = duration + interval %s
                                    WHERE game_id = %d
                                      AND player_id = %d;
                                    """,
                            duration2interval(Duration.between(this.lastDurationUpdate, LocalTime.now())),
                            this.gameID,
                            this.playerID[turn]
                    )
            );

        } catch (final SQLException _e) {
            System.err.printf("Saving duration of player %d has failed\n", turn);
            System.err.println(_e.getMessage());
            this.fail = true;
            this.backup.updateDuration(turn);
        }

        this.lastDurationUpdate = null;
    }

    private void update(final BlocksTable blocksTable, final Player[] players, final byte turn, final byte turnRotation, final Position choice, final Position matrixDie) {
        if (this.fail) {
            this.backup.save(blocksTable, players, turn, turnRotation, choice, matrixDie);
            return;
        }

        try {
            this.dbc.executeUpdate(String.format("""
                            UPDATE saved_games
                            SET turn          = %d,
                                turn_rotation = %d,
                                roll_column   = %d,
                                roll_row      = %d,
                                die_column    = %d,
                                die_row       = %d
                            WHERE g_save_id = %d;
                            """,
                    turn,
                    turnRotation,
                    choice.getX(),
                    choice.getY(),
                    matrixDie.getX(),
                    matrixDie.getY(),
                    this.gameID
            ));

            this.playerSaveID = new int[players.length];
            for (int order = 0; order < players.length; order++) {
                this.dbc.executeUpdate(String.format("""
                                        UPDATE saved_players_games
                                        SET plays       = %s,
                                            bombs       = %d,
                                            energy      = %d,
                                            energy_left = %d,
                                            bonus_score = %d
                                        WHERE pg_save_id = %d;
                                        """,
                                formatBool(players[order].isPlays()),
                                players[order].getBombs().get(),
                                players[order].getEnergy().getPosition(),
                                players[order].getEnergy().getAvailable(),
                                players[order].getBonusScore().getScale(),
                                order
                        )
                );

                this.resaveBoard(blocksTable, players[order].getBoard(), this.playerSaveID[order]);
            }

            this.updateDuration(turn);
            this.setDuration();


        } catch (final SQLException _e) {
            System.err.println("Global save has failed");
            System.err.println(_e.getMessage());
            this.fail = true;
            this.backup.save(blocksTable, players, turn, turnRotation, choice, matrixDie);
        }
    }

    private void resaveBoard(final BlocksTable blocksTable, final Board board, final int playerSaveID) throws SQLException {
        // Clearing up old save of the board
        this.dbc.executeUpdate(String.format("""
                                DELETE
                                FROM saved_boards sb
                                WHERE sb.pg_save_id = %d;
                                """,
                        playerSaveID
                )
        );

        for (final PlacedBlock block : board.getBoard()) {
            this.dbc.executeUpdate(String.format("""
                                    INSERT INTO saved_boards (pg_save_id, x, y, color, block)
                                    VALUES (%d, %d, %d, %d, %d);
                                    """,
                            playerSaveID,
                            block.getPlace().getX(),
                            block.getPlace().getY(),
                            block.getColor().ordinal(),
                            blocksTable.findOrigin(block) + 1
                    )
            );
        }

        final Color[][] energyBonus = board.getEnergyBonus();
        for (byte y = 0; y < energyBonus.length; y++) {
            for (byte x = 0; x < energyBonus[y].length; x++) {
                if (energyBonus[y][x] != null) {
                    this.dbc.executeUpdate(String.format("""
                                            INSERT INTO saved_boards (pg_save_id, x, y, color, block)
                                            VALUES (%d, %d, %d, %d, NULL);
                                            """,
                                    playerSaveID,
                                    x,
                                    y,
                                    energyBonus[y][x].ordinal()
                            )
                    );
                }
            }
        }
    }


    ///        Load
    @Override
    public List<PlayerLeaderboard> leaderboard(final int count) {
        if (this.fail) {
            return this.backup.leaderboard(count);
        }

        final List<PlayerLeaderboard> leaderboard = new ArrayList<>(count);
        try {
            setProperIntervalStandard(this.dbc);
            final ResultSet board = this.dbc.executeQuery(String.format("""
                                    SELECT p.name AS "username", g.start_dt AS "start", g.end_dt AS "end", pg.duration AS "duration", pg.score AS "score"
                                    FROM players_games pg
                                             INNER JOIN games g on g.game_id = pg.game_id
                                             INNER JOIN players p on p.player_id = pg.player_id
                                    WHERE g.duel IS FALSE
                                    ORDER BY pg.score DESC
                                    LIMIT %d;
                                    """,
                            count
                    )
            );

            while (board.next()) {
                final String name = unformatStr(board.getString("username"));
                final LocalDateTime startDT = board.getTimestamp("start").toLocalDateTime();
                final LocalDateTime endDT;
                {
                    final Timestamp tmpEndDT = board.getTimestamp("end");
                    if (tmpEndDT == null) {
                        endDT = null;
                    } else {
                        endDT = tmpEndDT.toLocalDateTime();
                    }
                }

                final String interval = board.getString("duration");
                if (interval == null) {
                    continue;
                }
                final Duration duration = interval2duration(interval);

                final short score = board.getShort("score");

                leaderboard.add(new PlayerLeaderboard(name, startDT, endDT, duration, score));
            }

        } catch (final SQLException _e) {
            System.err.println("Load leaderboard has failed");
            System.err.println(_e.getMessage());
            this.fail = true;
            return this.backup.leaderboard(count);
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
            int gameID = -1;
            List<String> names = new ArrayList<>(Brikks.MAX_PLAYERS);
            LocalDateTime start = null;

            final ResultSet variants = this.dbc.executeQuery("""
                    SELECT g.game_id AS "game_id", p.name AS "username", g.start_dt AS "start"
                    FROM saved_games sg
                             INNER JOIN games g ON g.game_id = sg.g_save_id
                             INNER JOIN players_games pg ON pg.game_id = g.game_id
                             INNER JOIN players p ON p.player_id = pg.player_id
                             INNER JOIN saved_players_games spg ON spg.pg_save_id = pg.pg_save_id
                    ORDER BY g.game_id, spg.player_order;
                    """
            );

            while (variants.next()) {
                final int checkID = variants.getInt("game_id");

                if (gameID == -1) {
                    gameID = checkID;
                } else if (checkID != gameID) {
                    gameID = checkID;
                    saved.add(new SavedGame(gameID, names, start));
                    names = new ArrayList<>(Brikks.MAX_PLAYERS);
                }

                start = variants.getTimestamp("start").toLocalDateTime();

                names.add(unformatStr(variants.getString("username")));
            }

            if (start != null) {
                saved.add(new SavedGame(gameID, names, start));
            }

        } catch (final SQLException _e) {
            System.err.println("Loading variants has failed");
            System.err.println(_e.getMessage());
            this.fail = true;
            return this.backup.load();
        }

        return saved;
    }

    @Override
    public LoadedGame load(final int ID, final BlocksTable blocksTable) {
        if (this.fail) {
            return this.backup.load(ID, blocksTable);
        }

        this.gameID = ID;

        final LoadedGame game;
        try {
            final List<Player> players = new ArrayList<>(Brikks.MAX_PLAYERS);
            final ResultSet gameSaved;
            {
                gameSaved = this.dbc.executeQuery(String.format("""
                                        SELECT spg.player_order,
                                               -- Player
                                               p.name           AS "username",
                                               spg.plays        AS "plays",
                                               -- Board
                                               sb.x             AS "x",
                                               sb.y             AS "y",
                                               sb.color         AS "color",
                                               sb.block         AS "block",
                                               -- Energy
                                               spg.energy       AS "energy",
                                               spg.energy_left  AS "energy_left",
                                               -- Bombs
                                               spg.bombs        AS "bombs",
                                               -- BonusScore
                                               spg.bonus_score  AS "bonus_score",
                                        
                                               -- matrixDie
                                               sg.die_column    AS "die_column",
                                               sg.die_row       AS "die_row",
                                               -- turn
                                               sg.turn          AS "turn",
                                               -- turnRotation
                                               sg.turn_rotation AS "turn_rotation",
                                               -- choice
                                               sg.roll_column   AS "choice_column",
                                               sg.roll_row      AS "choice_row",
                                               -- difficulty
                                               g.difficulty     AS "difficulty",
                                               -- duelMode
                                               g.duel           AS "duel_mode",
                                        
                                               -- save
                                               pg.player_id     AS "player_id",
                                               spg.pg_save_id   AS "pg_save_id",
                                               pg.duration      AS "duration"
                                        FROM saved_games sg
                                                 INNER JOIN games g ON g.game_id = sg.g_save_id
                                                 INNER JOIN players_games pg ON pg.game_id = g.game_id
                                                 INNER JOIN players p ON p.player_id = pg.player_id
                                                 INNER JOIN saved_players_games spg ON spg.pg_save_id = pg.pg_save_id
                                                 INNER JOIN saved_boards sb ON sb.pg_save_id = spg.pg_save_id
                                        WHERE sg.g_save_id = %d
                                        ORDER BY spg.player_order ASC;
                                        """,
                                this.gameID
                        )
                );
            }

            final List<Integer> playersID = new ArrayList<>(Brikks.MAX_PLAYERS);
            final List<Integer> playersSaveID = new ArrayList<>(Brikks.MAX_PLAYERS);

            Level difficulty = Level.TWO;
            boolean duelMode = false;
            byte turn = 0;
            byte turnRotation = 0;
            byte dieRow = 0;
            byte dieColumn = 0;
            byte rollX = 0;
            byte rollY = 0;
            {
                int playerID = -1;
                String name = null;
                boolean plays = false;

                BonusScore bonusScore = null;
                Energy energy = null;
                Bombs bombs = null;

                List<PlacedBlock> placedBlocks = new ArrayList<>();
                Color[][] energyBonus = new Color[Board.HEIGHT][Board.WIDTH];

                if (!gameSaved.next()) {
                    throw new SQLException(String.format("Nothing to load for gameID=%d", this.gameID));
                }
                int playerSaveID = gameSaved.getInt("pg_save_id");

                difficulty = Level.values()[gameSaved.getByte("difficulty")];
                duelMode = gameSaved.getBoolean("duel_mode");

                turn = gameSaved.getByte("turn");
                turnRotation = gameSaved.getByte("turn_rotation");
                dieColumn = gameSaved.getByte("die_column");
                dieRow = gameSaved.getByte("die_row");
                rollX = gameSaved.getByte("choice_column");
                rollY = gameSaved.getByte("choice_row");


                byte order = 0;
                do {
                    final int checkID = gameSaved.getInt("pg_save_id");

                    if (checkID != playerSaveID) {
                        playerSaveID = checkID;

                        players.add(
                                new Player(
                                        name,
                                        plays,
                                        new Board(bonusScore, energy, difficulty, placedBlocks, energyBonus),
                                        energy,
                                        bombs,
                                        bonusScore
                                )
                        );
                        playersID.add(playerID);
                        playersSaveID.add(playerSaveID);

                        order++;
                        placedBlocks = new ArrayList<>();
                        energyBonus = new Color[Board.HEIGHT][Board.WIDTH];
                        for (byte y = 0; y < energyBonus.length; y++) {
                            energyBonus[y] = new Color[Board.WIDTH];
                        }
                    }

                    // Player info
                    playerID = gameSaved.getInt("player_id");
                    name = unformatStr(gameSaved.getString("username"));
                    plays = gameSaved.getBoolean("plays");

                    // Player status
                    bonusScore = new BonusScore(gameSaved.getByte("bonus_score"));
                    energy = new Energy(bonusScore, gameSaved.getByte("energy"), gameSaved.getByte("energy_left"));
                    bombs = new Bombs(gameSaved.getByte("bombs"));


                    // Block creation
                    {
                        final byte x = gameSaved.getByte("x");
                        final byte y = gameSaved.getByte("y");
                        final byte color = gameSaved.getByte("color");

                        final byte blockID = gameSaved.getByte("block");
                        if (blockID == 0 /* == null */) {
                            energyBonus[y][x] = Color.values()[color];
                        } else {
                            placedBlocks.add(new PlacedBlock(
                                            color == Color.DUELER.ordinal() ?
                                                    BlocksTable.duelBlock
                                                    :
                                                    blocksTable.getBlock(new Position(
                                                                    (byte) ((blockID - 1) % BlocksTable.WIDTH),
                                                                    (byte) ((blockID - 1) / BlocksTable.WIDTH)
                                                            )
                                                    ),
                                            new Position(x, y)
                                    )
                            );
                        }
                    }
                } while (gameSaved.next());

                players.add(
                        new Player(
                                name,
                                plays,
                                new Board(bonusScore, energy, difficulty, placedBlocks, energyBonus),
                                energy,
                                bombs,
                                bonusScore
                        )
                );
                final byte count = (byte) players.size();
                this.playerID = new int[count];
                this.playerSaveID = new int[count];
                for (byte i = 0; i < count - 1; i++) {
                    this.playerID[i] = playersID.get(i);
                    this.playerSaveID[i] = playersSaveID.get(i);
                }
                this.playerID[count - 1] = playerID;
                this.playerSaveID[count - 1] = playerSaveID;
            }


            game = new LoadedGame(
                    players.toArray(Player[]::new),
                    new Position(dieRow, dieColumn),
                    turn,
                    turnRotation,
                    new Position(rollX, rollY),
                    difficulty,
                    duelMode
            );

        } catch (final SQLException _e) {
            System.err.println("Loading has failed");
            System.err.println(_e.getMessage());
            this.fail = true;
            return this.backup.load(ID, blocksTable);
        }

        this.firstSave = false;

        return game;
    }


    ///        End game
    @Override
    public void save(final byte order, final short score) {
        if (this.fail) {
            this.backup.save(order, score);
            return;
        }

        try {
            this.dbc.executeUpdate(String.format("""
                                    UPDATE players_games
                                    SET score = %d
                                    WHERE game_id = %d
                                      AND player_id = %d;
                                    -- & drop save
                                    DELETE
                                    FROM saved_players_games spg
                                    WHERE spg.pg_save_id = %d;
                                    """,
                            score,
                            this.gameID,
                            this.playerID[order],
                            this.playerSaveID[order]
                    )
            );
            // Respective parts of SAVED_BOARDS are deleted automatically with SAVED_PLAYERS_GAMES
            this.playerID[order] = -1;
            this.playerSaveID[order] = -1;

        } catch (final SQLException _e) {
            System.out.println("Final player save has failed");
            System.out.println(_e.getMessage());
            this.fail = true;
            this.backup.save(order, score);
        }
    }

    @Override
    public void dropSave() {
        if (this.fail) {
            this.backup.dropSave();
            return;
        }

        try {
            this.dbc.executeUpdate(String.format("""
                                    DELETE
                                    FROM saved_games sg
                                    WHERE sg.g_save_id = %d;
                                    """,
                            this.gameID
                    )
            );

        } catch (final SQLException _e) {
            System.err.println("Dropping the game save has failed");
            System.err.println(_e.getMessage());
            this.fail = true;
            this.backup.dropSave();
        }

        this.firstSave = true;
    }


    /// Support functions
    private static String formatStr(final String str) {
        // TODO: do something about problematic symbols in the name
        // �123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~
        return "'" + str + "'";
    }

    private static String unformatStr(final String str) {
        // TODO: do something about problematic symbols in the name
        return str;
    }

    private static String formatBool(final boolean bool) {
        return bool ? "TRUE" : "FALSE";
    }


    private static void setProperIntervalStandard(final DatabaseConnection dbc) throws SQLException {
        dbc.executeUpdate("SET intervalstyle = sql_standard;");
    }

    /**
     * Helping function of converting SQL's type INTERVAL to Java's type Duration.
     * <br>
     * Use
     * ```
     * SET intervalstyle = sql_standard;
     * ```
     * to make sure the proper INTERVAL standard is used !!!
     *
     * @param interval The DB's INTERVAL in text format according to `sql_standard`
     * @return java.time.Duration
     */
    private static Duration interval2duration(final String interval) {
        final long years, mons, days, hours, mins, secs, nanos;
        final String[] hoursMinsSecs;

        final String[] parsed = interval.split(" ", 3);
        switch (parsed.length) {
            case 3 -> {
                final String[] yearsMons = parsed[0].split("(?<=\\d)-(?=\\d)", 2);
                years = Long.parseLong(yearsMons[0]);
                mons = Long.parseLong(yearsMons[1]);

                days = Long.parseLong(parsed[1]);

                hoursMinsSecs = parsed[2].split(":", 3);
            }
            case 2 -> {
                years = 0;
                mons = 0;

                days = Long.parseLong(parsed[0]);

                hoursMinsSecs = parsed[1].split(":", 3);
            }
            case 1 -> {
                years = 0;
                mons = 0;

                days = 0;

                hoursMinsSecs = parsed[0].split(":", 3);
            }
            default -> {
                years = 0;
                mons = 0;

                days = 0;

                hoursMinsSecs = new String[]{"0", "0", "0"};
            }
        }

        final String[] secsNanos;
        switch (hoursMinsSecs.length) {
            case 3 -> {
                hours = Long.parseLong(hoursMinsSecs[0]);
                mins = Long.parseLong(hoursMinsSecs[1]);
                secsNanos = hoursMinsSecs[2].split("\\.", 2);
            }
            case 2 -> {
                hours = 0;
                mins = Long.parseLong(hoursMinsSecs[0]);
                secsNanos = hoursMinsSecs[1].split("\\.", 2);
            }
            case 1 -> {
                hours = 0;
                mins = 0;
                secsNanos = hoursMinsSecs[0].split("\\.", 2);
            }
            default -> {
                hours = 0;
                mins = 0;
                secsNanos = new String[]{"0"};
            }
        }

        secs = Long.parseLong(secsNanos[0]);
        if (secsNanos.length == 2) {
            if (secsNanos[1].length() > 9) {
                nanos = Long.parseLong(secsNanos[1].substring(0, 9));
            } else {
                nanos = Long.parseLong(secsNanos[1] + "0".repeat(9 - secsNanos[1].length()));
            }
        } else {
            nanos = 0;
        }

        return Duration.ZERO
                .plusDays(years * 365)
                .plusDays(mons * 30)
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(mins)
                .plusSeconds(secs)
                .plusNanos(nanos);
    }

    /**
     * Helping function of converting Java's type Duration to SQL's type INTERVAL.
     * The word 'INTERVAL' not included.
     * <br>
     * Use
     * ```
     * SET intervalstyle = sql_standard;
     * ```
     * to make sure the proper INTERVAL standard is used !!!
     *
     * @param duration java.time.Duration
     * @return The DB's INTERVAL in text format according to `sql_standard`
     */
    private static String duration2interval(final Duration duration) {
        return String.format("'%d.%09d'", duration.getSeconds(), duration.getNano());
    }
}
