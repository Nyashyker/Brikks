--- FIRST SAVE
-- does player exist ?
SELECT *
FROM players p
WHERE p.name = ''
LIMIT 1;

-- create game
INSERT INTO games (start_dt, end_dt, difficulty, duel)
VALUES (NOW(), NULL, 0, FALSE)
RETURNING game_id AS "game_id";
-- get player id
WITH new_player_id AS (INSERT INTO players (name) VALUES ('Alice')
    ON CONFLICT (name) DO NOTHING RETURNING player_id)
SELECT player_id
FROM new_player_id
UNION ALL
SELECT p.player_id
FROM players p
WHERE p.name = 'Alice'
LIMIT 1;
-- per player
INSERT INTO players_games (game_id, player_id, pg_save_id, duration, score)
VALUES (0, 0, NULL, interval '0.0', NULL);

-- save game
INSERT INTO saved_games (g_save_id, turn, turn_rotation, roll_column, roll_row, die_column, die_row)
VALUES (0, 0, 0, 1, 2, 1, 2);
-- per player
INSERT INTO saved_players_games (plays, bombs, energy, energy_left, bonus_score, player_order)
VALUES (TRUE, 2, 7, 3, 2, 0)
RETURNING pg_save_id;
UPDATE players_games
SET pg_save_id = 2,
    duration= interval '0.0'
WHERE game_id = 2
  AND player_id = 2;
-- board
INSERT INTO saved_boards (pg_save_id, x, y, color, block)
VALUES (1, 0, 0, 0, NULL);

--- UPDATE
-- update game
UPDATE saved_games
SET turn=0,
    turn_rotation=0,
    roll_column=0,
    roll_row=0,
    die_column=0,
    die_row=0
WHERE g_save_id = 1;
-- per player
UPDATE players_games
SET duration = duration + interval '0.0'
WHERE game_id = 2
  AND player_id = 2;
UPDATE saved_players_games
SET plays= TRUE,
    bombs=0,
    energy=0,
    energy_left=0,
    bonus_score=0
WHERE pg_save_id = 0;
-- board
UPDATE saved_boards
SET x=1,
    y=1,
    color=1,
    block=1
WHERE pg_save_id = 1;

--- LOAD
-- leaderboard
SELECT p.name AS "username", g.start_dt AS "start", g.end_dt AS "end", pg.duration AS "duration", pg.score AS "score"
FROM players_games pg
         INNER JOIN games g on g.game_id = pg.game_id
         INNER JOIN players p on p.player_id = pg.player_id
WHERE g.duel IS FALSE
ORDER BY pg.score DESC
LIMIT 7;

-- variants to load
SELECT g.game_id AS "game_id", p.name AS "username", g.start_dt AS "start"
FROM saved_games sg
         INNER JOIN games g ON g.game_id = sg.g_save_id
         INNER JOIN players_games pg ON pg.game_id = g.game_id
         INNER JOIN players p ON p.player_id = pg.player_id
         INNER JOIN saved_players_games spg ON spg.pg_save_id = pg.pg_save_id
ORDER BY g.game_id, spg.player_order;

-- load
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
WHERE sg.g_save_id = 2
ORDER BY spg.player_order ASC;

-- END
-- save result
UPDATE players_games
SET score = 10
WHERE game_id = 2
  AND player_id = 2;
-- drop save
DELETE
FROM saved_games sg
WHERE sg.g_save_id = 2;
DELETE
FROM saved_players_games spg
WHERE spg.pg_save_id IN
      (SELECT pg.pg_save_id FROM players_games pg WHERE pg.game_id = 2);

---

SELECT *
FROM players;
SELECT *
FROM games;
SELECT *
FROM players_games;
SELECT *
FROM saved_games;
SELECT *
FROM saved_players_games;
SELECT *
FROM saved_boards;
