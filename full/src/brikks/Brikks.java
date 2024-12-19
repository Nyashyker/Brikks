package brikks;

import brikks.container.*;
import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.save.*;
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
        this.save.saveStartDateTime();

        // Creating the players
        final byte playerCount = this.view.askPlayerCount(Brikks.MAX_PLAYERS);
        final String[] name$s = new String[playerCount];
        final Level difficulty = this.view.askDifficulty();

        final Player[] players = new Player[playerCount];
        for (byte i = 0; i < playerCount; i++) {
            String name;
            do {
                name = this.view.askName();
            } while (this.save.playerExists(name) && !this.view.askUseExistingPlayer(name));

            players[i] = new Player(this.save.getPlayerSave(name), name, (byte) name$s.length, difficulty);
        }

        // Each player has to chose unique starting block
        final Position[] taken = new Position[playerCount];
        for (byte i = 0; i < playerCount; i++) {
            Position firstChoice;
            boolean guessUntaken;
            do {
                firstChoice = this.view.askFirstChoice(this.blocksTable);
                guessUntaken = true;
                for (byte t = 0; t < i; t++) {
                    if (taken[t].equals(firstChoice)) {
                        guessUntaken = false;
                        this.view.firstChoiceTaken();
                        break;
                    }
                }
            } while (!guessUntaken);

            taken[i] = firstChoice;
            if (!players[i].firstChoice(this.view, this.blocksTable.getBlock(firstChoice))) {
                return;
            }
        }


        // Activating duel mode
        final boolean duelMode;
        if (playerCount == 2) {
            duelMode = this.view.askDuel();
        } else {
            duelMode = false;
        }
        this.save.save(duelMode);

        // Run the game
        final RunsResults results = this.run(players, duelMode);
        if (!results.endGame()) {
            return;
        }

        // Ending the game
        this.save.saveEndDateTime();
        for (Player player : players) {
            player.saveFinal();
        }

        if (playerCount == 1) {
            this.view.endSolo(this.save.getRanks(), players[0].calculateFinal());
        } else if (duelMode) {
            final ByteLoop loop = new ByteLoop(results.duelWinner(), (byte) 2);
            this.view.endDuel(players[loop.current()], players[loop.forecast()]);
        } else {
            this.view.endStandard(players);
        }
    }

    public void load() {
        // TODO: The load functional
    }


    private RunsResults run(final Player[] players, final boolean duelMode) {
        final ByteLoop loopingLoop = new ByteLoop((byte) players.length);
        final ByteLoop loop = new ByteLoop((byte) players.length);

        boolean stillPlays;
        do {
            stillPlays = false;
            loop.setPosition(loopingLoop.goForward());

            boolean first = true;
            for (; loop.loopedForward(); loop.goForward()) {
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
            }
        } while (stillPlays);

        return new RunsResults(true, (byte) -1);
    }
}
