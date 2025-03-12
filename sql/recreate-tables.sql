DROP TABLE IF EXISTS players_games;
DROP TABLE IF EXISTS saved_boards;
DROP TABLE IF EXISTS blocks;
DROP TABLE IF EXISTS saved_players_games;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS saved_games;

CREATE TABLE IF NOT EXISTS saved_games
(
    save_id     SERIAL
        CONSTRAINT pk_save_save_id PRIMARY KEY,
    turn        SMALLINT
        CONSTRAINT nn_turn NOT NULL
        CONSTRAINT ch_turn CHECK ( turn >= 0 AND turn < 4 ),
    roll_column SMALLINT
        CONSTRAINT nn_roll_column NOT NULL
        CONSTRAINT ch_roll_column CHECK ( roll_column >= 0 AND roll_column < 4 ),
    roll_row    SMALLINT
        CONSTRAINT nn_roll_row NOT NULL
        CONSTRAINT ch_roll_row CHECK ( roll_row >= 0 AND roll_row < 6 ),
    die_column  SMALLINT
        CONSTRAINT nn_die_column NOT NULL
        CONSTRAINT ch_die_column CHECK ( die_column >= 0 AND die_column < 4 ),
    die_row     SMALLINT
        CONSTRAINT nn_die_row NOT NULL
        CONSTRAINT ch_die_row CHECK ( die_row >= 0 AND die_row < 6 )
);

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
        CONSTRAINT nn_duel NOT NULL,
    save_id    INT
        CONSTRAINT fk_save_game_id REFERENCES saved_games (save_id)
            ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS players
(
    player_id SERIAL
        CONSTRAINT pk_player_id PRIMARY KEY,
    name      VARCHAR(37)
        CONSTRAINT nn_name NOT NULL
);

CREATE TABLE IF NOT EXISTS saved_players_games
(
    save_id      SERIAL
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
        CONSTRAINT ch_block CHECK ( block > 0 AND block <= 25 ),
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
        CONSTRAINT ch_energy_bonus CHECK ( energy_bonus > 0 AND energy_bonus <= 6 ),
    block        SMALLINT
        CONSTRAINT nn_block NOT NULL
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


INSERT INTO players (name) VALUES ('testuval''nyk 0');
INSERT INTO players (name) VALUES ('testuval''nyk 2');
INSERT INTO players (name) VALUES ('testuval''nyk 3');
INSERT INTO games (start_dt, end_dt, difficulty, duel, save_id)
VALUES (TO_DATE('2025-01-07 12:00', 'YYYY-MM-DD HH:MI'), NOW(), 0, FALSE, NULL);
INSERT INTO saved_players_games (save_id, plays, bombs, energy, energy_left, bonus_score, player_order)
VALUES (1,FALSE, 0, 0, 0, 0, 0);
INSERT INTO saved_players_games (save_id, plays, bombs, energy, energy_left, bonus_score, player_order)
VALUES (2,TRUE, 0, 0, 0, 0, 1);
INSERT INTO saved_players_games (save_id, plays, bombs, energy, energy_left, bonus_score, player_order)
VALUES (3,TRUE, 0, 4, 0, 0, 3);
INSERT INTO players_games (game_id, player_id, save_id, duration, score)
VALUES (1, 1, 1, NOW()-TO_DATE('2025-01-05 12:00', 'YYYY-MM-DD HH24:MI'), 123);
INSERT INTO players_games (game_id, player_id, save_id, duration, score)
VALUES (1, 2, 2, NOW()-TO_DATE('2025-01-05 11:00', 'YYYY-MM-DD HH24:MI'), 150);
INSERT INTO players_games (game_id, player_id, save_id, duration, score)
VALUES (1, 3, 3, NOW()-TO_DATE('2025-01-05 13:00', 'YYYY-MM-DD HH24:MI'), 190);
INSERT INTO saved_boards (save_id, x, y, energy_bonus, block)
VALUES (1, 0, 0, 1, 24);
SELECT * FROM saved_players_games;
DELETE FROM saved_players_games WHERE save_id IN (SELECT save_id FROM players_games WHERE game_id = 1);
SELECT * FROM saved_boards;
SELECT * FROM players_games;

SELECT g.game_id
FROM games g
WHERE g.end_dt IS NULL;

UPDATE saved_players_games SET player_order=2 WHERE player_order=0;

SELECT sb.x, sb.y, sb.energy_bonus, b.block, b.table_row, b.table_column
FROM saved_boards sb
LEFT OUTER JOIN blocks b on b.block = sb.block
WHERE sb.save_id=1
ORDER BY sb.y DESC;

SELECT sg.turn, sg.die_row, sg.die_column, sg.roll_row, sg.roll_column
FROM saved_games sg
WHERE sg.save_id=1;

SELECT player_id FROM players WHERE name='%s';
SELECT * FROM players;
SELECT * FROM games;
SELECT * FROM players_games;
SELECT * FROM saved_games;
SELECT * FROM saved_players_games;
