DROP TABLE players;
CREATE TABLE players (
    player_id INTEGER CONSTRAINT player_pk PRIMARY KEY CONSTRAINT player_nn NOT NULL,
    name VARCHAR(16) CONSTRAINT name_nn NOT NULL,
    player_highscore NUMERIC(191) CONSTRAINT p_highscore_nn NOT NULL
);
