DROP TABLE IF EXISTS players_games;
DROP TABLE IF EXISTS saved_boards;
DROP TABLE IF EXISTS saved_players_games;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS saved_games;
DROP TABLE IF EXISTS games;


CREATE TABLE IF NOT EXISTS games
(
    game_id    SERIAL
        CONSTRAINT pk_game_id PRIMARY KEY,
    start_dt   TIMESTAMP
        CONSTRAINT nn_start NOT NULL,
    end_dt     TIMESTAMP,
    difficulty SMALLINT
        CONSTRAINT nn_difficulty NOT NULL
        CONSTRAINT ch_difficulty CHECK ( difficulty >= 0 AND difficulty < 4 ),
    duel       BOOLEAN
        CONSTRAINT nn_duel NOT NULL
);

CREATE TABLE IF NOT EXISTS saved_games
(
    g_save_id     INT
        CONSTRAINT pk_g_save_id PRIMARY KEY
        CONSTRAINT fk_g_save_id REFERENCES games (game_id)
            ON DELETE RESTRICT,
    turn          SMALLINT
        CONSTRAINT nn_turn NOT NULL
        CONSTRAINT ch_turn CHECK ( turn >= 0 AND turn < 4 ),
    turn_rotation SMALLINT
        CONSTRAINT nn_turn_rotation NOT NULL
        CONSTRAINT ch_turn_rotation CHECK ( turn >= 0 AND turn < 4 ),
    roll_column   SMALLINT
        CONSTRAINT nn_roll_column NOT NULL
        CONSTRAINT ch_roll_column CHECK ( roll_column >= 0 AND roll_column < 4 ),
    roll_row      SMALLINT
        CONSTRAINT nn_roll_row NOT NULL
        CONSTRAINT ch_roll_row CHECK ( roll_row >= 0 AND roll_row < 6 ),
    die_column    SMALLINT
        CONSTRAINT nn_die_column NOT NULL
        CONSTRAINT ch_die_column CHECK ( die_column >= 0 AND die_column < 4 ),
    die_row       SMALLINT
        CONSTRAINT nn_die_row NOT NULL
        CONSTRAINT ch_die_row CHECK ( die_row >= 0 AND die_row < 6 )
);

CREATE TABLE IF NOT EXISTS players
(
    player_id SERIAL
        CONSTRAINT pk_player_id PRIMARY KEY,
    name      VARCHAR(37)
        CONSTRAINT nn_name NOT NULL
        CONSTRAINT uq_name UNIQUE
);

CREATE TABLE IF NOT EXISTS saved_players_games
(
    pg_save_id   SERIAL
        CONSTRAINT pk_player_game_save_player_id PRIMARY KEY,
    plays        BOOLEAN
        CONSTRAINT nn_plays NOT NULL,
    bombs        SMALLINT
        CONSTRAINT nn_bombs NOT NULL
        CONSTRAINT ch_bombs CHECK ( bombs >= 0 AND bombs <= 3 ),
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

CREATE TABLE IF NOT EXISTS saved_boards
(
    pg_save_id INT
        CONSTRAINT fk_player_game_save_id REFERENCES saved_players_games (pg_save_id)
            ON DELETE CASCADE,
    x          SMALLINT
        CONSTRAINT ch_x CHECK ( x >= 0 AND x < 10 ),
    y          SMALLINT
        CONSTRAINT ch_y CHECK ( y >= 0 AND y < 11 ),
    color      SMALLINT
        CONSTRAINT nn_color NOT NULL
        CONSTRAINT ch_color CHECK ( color >= 0 AND color < 7 ),
    block      SMALLINT
        CONSTRAINT ch_block CHECK ( block > 0 AND block <= 25 ),
    CONSTRAINT pk_save_cell PRIMARY KEY (pg_save_id, x, y)
);

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
        CONSTRAINT ch_score CHECK ( score >= 0 AND score <= 191 ),
    CONSTRAINT pk_player_game PRIMARY KEY (player_id, game_id)
);
