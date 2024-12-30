package brikks;

import brikks.container.*;
import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.save.*;
import brikks.save.container.LoadedGame;
import brikks.save.container.SavedGame;
import brikks.view.*;
import circle_loop.ByteLoop;

public class Brikks {
    public static final byte MAX_PLAYERS = 4;


    private final BlocksTable blocksTable;
    private final MatrixDice matrixDie;

    private final Save save;
    private final View view;


    public Brikks(View view, Save save, Block[][] blocksTable) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        if (save == null) {
            throw new IllegalArgumentException("save cannot be null");
        }
        if (blocksTable == null) {
            throw new IllegalArgumentException("blocksTable cannot be null");
        }
        if (blocksTable.length == 0 || blocksTable[0].length == 0) {
            throw new IllegalArgumentException("blocksTable cannot be empty");
        }

        this.blocksTable = new BlocksTable(blocksTable);
        this.matrixDie = new MatrixDice(BlocksTable.WIDTH, BlocksTable.HEIGHT);

        this.save = save;
        this.view = view;
    }


    public void menu() {
        boolean exit = false;
        while (!exit) {
            switch (this.view.menu()) {
                case NEW_GAME -> this.start();
                case LOAD -> this.load();
                case LIDERBOARD -> this.liderboard();
                case EXIT -> exit = true;
            }
        }

        this.view.exit();
    }

    public void liderboard() {
        this.view.liderboard(this.save.liderboard());
    }

    public void start() {
        // Creating the players
        final byte playerCount = this.view.askPlayerCount(Brikks.MAX_PLAYERS);
        if (playerCount == 0) {
            return;
        }

        final Level difficulty = this.view.askDifficulty();
        if (difficulty == null) {
            return;
        }
        this.save.save(difficulty);

        final Player[] players = new Player[playerCount];
        {
            final String[] name$s = new String[playerCount];

            for (byte i = 0; i < playerCount; i++) {
                String name;
                boolean nameDecided = false;
                do {
                    name = this.view.askName((byte) (i + 1));
                    if (name == null) {
                        return;
                    }

                    final boolean nameExists = this.save.playerExists(name);
                    if (!nameExists) {
                        nameDecided = true;
                    } else if (this.view.askUseExistingPlayer(name)) {
                        nameDecided = true;
                    }
                } while (!nameDecided);

                players[i] = new Player(this.save.getPlayerSave(name), name, (byte) name$s.length, difficulty);
            }
        }

        this.firstChoice(players);

        // Activating duel mode
        final boolean duelMode;
        if (playerCount == 2) {
            duelMode = this.view.askDuel();
        } else {
            duelMode = false;
        }
        this.save.save(duelMode);

        // Run the game
        this.save.saveStartDateTime();
        this.save.startCountingTime();

        final RunsResults results;
        if (playerCount == 1) {
            results = this.run(players[0]);
        } else {
            results = this.run(players, duelMode);
        }

        if (results.endGame()) {
            this.end(players, difficulty, duelMode, results.duelWinnerIndex());
        }
    }

    public void load() {
        final Player[] players;
        final Level difficulty;
        final boolean duelMode;
        {
            final SavedGame[] variants = this.save.savedGames();
            final SavedGame choice = this.view.askChoiceSave(variants);
            if (choice == null) {
                return;
            }

            final LoadedGame loaded = this.save.loadGame(choice.ID());

            players = loaded.players();
            this.matrixDie.cheat(loaded.matrixDie());
            difficulty = loaded.difficulty();
            duelMode = loaded.duel();
        }

        this.save.startCountingTime();
        final RunsResults results = this.run(players, duelMode);
        if (results.endGame()) {
            this.end(players, difficulty, duelMode, results.duelWinnerIndex());
        }
    }


    private void firstChoice(final Player[] players) {
        final Position[] taken = new Position[players.length];

        final ByteLoop looperRow = new ByteLoop(BlocksTable.WIDTH);
        final ByteLoop looperColumn = new ByteLoop(BlocksTable.HEIGHT);
        for (byte i = 0; i < players.length; i++) {
            Position firstChoice = this.matrixDie.roll();

            // Ensure that there are no duplicates
            looperRow.setPosition(firstChoice.getX());
            looperColumn.setPosition(firstChoice.getY());
            boolean guessTaken;
            do {
                guessTaken = false;
                for (byte t = 0; t < i; t++) {
                    if (taken[t].equals(firstChoice)) {
                        guessTaken = true;
                        firstChoice = new Position(looperRow.goForward(), looperRow.goForward());
                        break;
                    }
                }
            } while (guessTaken);

            taken[i] = firstChoice;
            players[i].firstChoice(this.blocksTable.getBlock(firstChoice));
        }
    }

    private RunsResults run(final Player player) {
        while (true) {
            this.save.updateDuration();
            this.view.draw(player);

            final TurnsResults result = player.turn(this.view, this.blocksTable, new Position(this.matrixDie.get()));

            if (result.exit()) {
                return new RunsResults(false, (byte) -1);
            } else if (result.giveUp()) {
                player.saveFinal();
                return new RunsResults(true, (byte) -1);
            }
        }
    }

    private RunsResults run(final Player[] players, final boolean duelMode) {
        final ByteLoop loopingLoop = new ByteLoop((byte) players.length);
        final ByteLoop loop = new ByteLoop((byte) players.length);

        boolean stillPlays;
        do {
            stillPlays = false;
            loop.setPosition(loopingLoop.goForward());

            boolean first = true;
            for (; !loop.loopedForward(); loop.goForward()) {
                final Player player = players[loop.current()];

                if (!player.isPlays()) {
                    continue;
                }

                stillPlays = true;
                this.save.updateDuration();
                this.view.draw(player);

                final TurnsResults result;
                if (first) {
                    result = player.turn(this.view, this.blocksTable, this.matrixDie);
                    first = false;
                } else {
                    result = player.turn(this.view, this.blocksTable, new Position(this.matrixDie.get()));
                }

                if (result.exit()) {
                    return new RunsResults(false, (byte) -1);
                } else if (result.giveUp()) {
                    player.saveFinal();
                    if (duelMode) {
                        return new RunsResults(true, loop.backcast());
                    }
                }

                if (duelMode && result.duelBonus() > 0) {
                    player.duelTurn(this.view, players[loop.forecast()], result.duelBonus());
                }
            }
        } while (stillPlays);

        return new RunsResults(true, (byte) -1);
    }

    private void end(final Player[] players, final Level difficulty, final boolean duelMode, final byte winnerIndex) {
        this.save.saveEndDateTime();
        for (final Player player : players) {
            player.saveFinal();
        }

        if (players.length == 1) {
            this.view.endSolo(players[0].name, players[0].calculateFinal(), difficulty);
        } else if (duelMode) {
            final ByteLoop loop = new ByteLoop(winnerIndex, (byte) 2);
            this.view.endDuel(players[loop.current()].name, players[loop.forecast()].name);
        } else {
            this.view.endStandard(players);
        }
    }
}
