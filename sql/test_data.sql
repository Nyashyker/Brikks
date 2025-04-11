-- --------------------------------------------------
-- 1) ДОДАЄМО ГРАВЦІВ (players)
-- --------------------------------------------------
INSERT INTO players (player_id, name)
VALUES
    (1, 'Alice'),
    (2, 'Bob'),
    (3, 'Charlie'),
    (4, 'Diana');


-- --------------------------------------------------
-- 2) ДОДАЄМО ІГРИ (games)
--    - Гра №1: дуель (2 гравці), НЕ завершена, без збереження
--    - Гра №2: НЕ завершена, НЕ дуель (3 гравці), має збереження
--    - Гра №3: завершена (кілька гравців), без збереження
--    - Гра №4: завершена (один гравець), без збереження
-- --------------------------------------------------
INSERT INTO games (game_id, start_dt, difficulty, duel)
VALUES
    (1, '2025-04-01 12:00:00', 2, TRUE),
    (2, '2025-04-01 13:00:00', 1, FALSE);

INSERT INTO games (game_id, start_dt, end_dt, difficulty, duel)
VALUES
    (3, '2025-04-02 14:00:00', '2025-04-02 16:30:00', 0, FALSE),
    (4, '2025-04-03 09:00:00', '2025-04-03 10:00:00', 2, FALSE);


-- --------------------------------------------------
-- 3) ЗБЕРЕЖЕННЯ ДЛЯ ГРИ №2
--    Спершу створюємо запис у saved_games,
--    потім у saved_players_games,
--    бо players_games.pg_save_id залежить від того, що ми тут вставимо.
-- --------------------------------------------------

-- 3.1) "Шапка" збереження гри №2 (saved_games).
--      g_save_id = game_id = 2. Наразі хід у player_order=1.
INSERT INTO saved_games (
    g_save_id,
    turn,
    turn_rotation,
    roll_column,
    roll_row,
    die_column,
    die_row
)
VALUES
    (2, 1, 2, 2, 3, 1, 4);

-- 3.2) Збереження "гравець-гра" (saved_players_games)
--      Для гри №2 припустимо 3 гравці (приклад). Кожному свій pg_save_id.
--      Зверніть увагу, що exactly один із player_order=1 (бо turn=1).
INSERT INTO saved_players_games (
    pg_save_id,
    plays,
    bombs,
    energy,
    energy_left,
    bonus_score,
    player_order
)
VALUES
    (10, TRUE, 1, 10, 8, 0, 0),  -- player_order=0
    (11, TRUE, 0, 12, 9, 2, 1),  -- player_order=1 (поточний хід)
    (12, TRUE, 2, 11, 7, 1, 2);  -- player_order=2


-- --------------------------------------------------
-- 4) ЗВ'ЯЗОК ГРАВЦІВ З ІГРАМИ (players_games)
--    Тепер можемо прив'язувати pg_save_id до гри №2,
--    адже saved_players_games уже існує.
-- --------------------------------------------------

-- Гра №2 (не завершена) з трьома гравцями: 1 (Alice), 2 (Bob), 3 (Charlie).
-- Для кожного вказано свій pg_save_id.
INSERT INTO players_games (game_id, player_id, pg_save_id)
VALUES
    (2, 1, 10),
    (2, 2, 11),
    (2, 3, 12);

-- Гра №1 (дуель, 2 гравці, без збереження => pg_save_id не вказуємо).
INSERT INTO players_games (game_id, player_id)
VALUES
    (1, 1),
    (1, 4);

-- Гра №3 (завершена, кілька гравців). Заповнюємо end_dt, duration, score:
INSERT INTO players_games (game_id, player_id, duration, score)
VALUES
    (3, 2, '02:30:00', 50),
    (3, 3, '02:30:00', 45);

-- Гра №4 (завершена, один гравець). end_dt, duration, score:
INSERT INTO players_games (game_id, player_id, duration, score)
VALUES
    (4, 4, '01:00:00', 120);


-- --------------------------------------------------
-- 5) ЗБЕРЕЖЕННЯ ДОШКИ (saved_boards)
--    На кожне pg_save_id - свій набір (x,y).
--    Пам'ятаємо, що (x,y) у межах одного g_save_id не дублюються між різними pg_save_id,
--    бо "на кожну пару x+y є рівно одне pg_save_id".
-- --------------------------------------------------

-- Для pg_save_id=10 (гравець 1 у грі №2)
INSERT INTO saved_boards (pg_save_id, x, y, color, block)
VALUES
    (10, 0, 0, 2, 1),
    (10, 1, 0, 3, 5),
    (10, 2, 0, 6, 25),
    (10, 0, 1, 4, NULL),
    (10, 1, 1, 5, 2),
    (10, 2, 1, 1, NULL);

-- Для pg_save_id=11 (гравець 2 у грі №2)
INSERT INTO saved_boards (pg_save_id, x, y, color, block)
VALUES
    (11, 0, 2, 3, 2),
    (11, 1, 2, 2, NULL),
    (11, 2, 2, 6, 10),
    (11, 0, 3, 5, 25),
    (11, 1, 3, 1, 3),
    (11, 2, 3, 4, NULL);

-- Для pg_save_id=12 (гравець 3 у грі №2)
INSERT INTO saved_boards (pg_save_id, x, y, color, block)
VALUES
    (12, 0, 4, 1, 2),
    (12, 1, 4, 2, NULL),
    (12, 2, 4, 3, 4),
    (12, 0, 5, 6, NULL),
    (12, 1, 5, 5, 25),
    (12, 2, 5, 4, 10);
