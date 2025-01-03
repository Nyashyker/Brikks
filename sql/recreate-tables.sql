DROP TABLE IF EXISTS saved_boards;
DROP TABLE IF EXISTS players_games;
DROP TABLE IF EXISTS saved_players_games;
DROP TABLE IF EXISTS blocks;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS saved_games;

CREATE TABLE players
(
    player_id SERIAL
        CONSTRAINT pk_player_id PRIMARY KEY,
    name      VARCHAR(37)
        CONSTRAINT nn_name NOT NULL
);

CREATE TABLE saved_players_games
(
    save_id      SERIAL
        CONSTRAINT pk_save_player_id PRIMARY KEY,
    plays        BOOLEAN
        CONSTRAINT nn_plays NOT NULL,
    bombs        NUMERIC(4)
        CONSTRAINT nn_bombs NOT NULL,
    energy       NUMERIC(28)
        CONSTRAINT nn_energy NOT NULL,
    energy_left  NUMERIC(29)
        CONSTRAINT nn_energy_left NOT NULL,
    bonus_score  NUMERIC(15)
        CONSTRAINT nn_bonus_score NOT NULL,
    player_order NUMERIC(4)
        CONSTRAINT nn_order NOT NULL
);

CREATE TABLE saved_games
(
    save_id     SERIAL
        CONSTRAINT pk_save_save_id PRIMARY KEY,
    turn        NUMERIC(4)
        --        CONSTRAINT fk_turn REFERENCES saved_players_games (player_order)
--            ON DELETE CASCADE
        CONSTRAINT nn_turn NOT NULL,
    roll_column NUMERIC(4)
        CONSTRAINT nn_roll_column NOT NULL,
    roll_row    NUMERIC(6)
        CONSTRAINT nn_roll_row NOT NULL,
    die_column  NUMERIC(4)
        CONSTRAINT nn_die_column NOT NULL,
    die_row     NUMERIC(6)
        CONSTRAINT nn_die_row NOT NULL
);

CREATE TABLE games
(
    game_id    SERIAL
        CONSTRAINT pk_game_id PRIMARY KEY,
    start_dt   TIMESTAMP
        CONSTRAINT nn_start NOT NULL,
    end_dt     TIMESTAMP,
    difficulty NUMERIC(4)
        CONSTRAINT nn_difficulty NOT NULL,
    duel       BOOLEAN
        CONSTRAINT nn_duel NOT NULL,
    save_id    INT
        CONSTRAINT fk_save_id REFERENCES saved_games (save_id)
            ON DELETE SET NULL
);

CREATE TABLE players_games
(
    game_id   INT
        CONSTRAINT fk_game_id REFERENCES games (game_id)
            ON DELETE RESTRICT,
    player_id INT
        CONSTRAINT fk_player_id REFERENCES players (player_id)
            ON DELETE RESTRICT,
    save_id   INT
        CONSTRAINT fk_save_id REFERENCES saved_players_games (save_id)
            ON DELETE SET NULL,
    duration  INTERVAL,
    score     NUMERIC(191),
    CONSTRAINT pk_player_game PRIMARY KEY (game_id, player_id)
);

CREATE TABLE blocks
(
    block        NUMERIC(25)
        CONSTRAINT pk_block PRIMARY KEY,
    table_column NUMERIC(4),
    table_row    NUMERIC(6)
);

CREATE TABLE saved_boards
(
    save_id      INT
        CONSTRAINT fk_save_id REFERENCES saved_players_games (save_id)
            ON DELETE CASCADE,
    x            NUMERIC(4),
    y            NUMERIC(6),
    energy_bonus NUMERIC(6)
        CONSTRAINT nn_energy_bonus NOT NULL,
    block        NUMERIC(25)
        CONSTRAINT fk_block REFERENCES blocks (block)
            ON DELETE RESTRICT,
    CONSTRAINT pk_save_cell PRIMARY KEY (save_id, x, y)
);
