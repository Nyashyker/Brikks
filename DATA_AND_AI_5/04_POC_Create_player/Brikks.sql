CREATE TABLE players (
    player_id SERIAL CONSTRAINT player_pk PRIMARY KEY CONSTRAINT player_nn NOT NULL,
    name VARCHAR(16) CONSTRAINT name_nn NOT NULL
);
