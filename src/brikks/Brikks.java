package brikks;

import brikks.container.RunsResults;
import brikks.container.TurnsResults;
import brikks.essentials.Block;
import brikks.essentials.Loop;
import brikks.essentials.MatrixDice;
import brikks.essentials.Position;
import brikks.essentials.enums.Level;
import brikks.save.PlayerSave;
import brikks.save.Save;
import brikks.save.container.LoadedGame;
import brikks.save.container.SavedGame;
import brikks.view.View;

import java.util.List;


public class Brikks implements GameSave {
    public static final byte MAX_PLAYERS = 4;


    private final BlocksTable blocksTable;
    private final MatrixDice matrixDie;

    private final Save saver;
    private final View view;

    private Player[] players;
    private byte turn;
    private boolean firstSave;


    public Brikks(View view, Save saver, Block[][] blocksTable) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        if (saver == null) {
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

        this.saver = saver;
        this.view = view;

        this.players = null;
        this.turn = -1;
        this.firstSave = true;
    }


    public void menu() {
        boolean exit = false;
        while (!exit) {
            switch (this.view.menu()) {
                case NEW_GAME -> this.start();
                case LOAD -> this.load();
                case LEADERBOARD -> this.leaderboard();
                case EXIT -> exit = true;
            }
        }

        this.view.exit();
    }

    public void leaderboard() {
        this.view.leaderboard(this.saver.leaderboard());
        // TODO: there should be search by name (player), game mode (solo, duel, or standard) ect
    }

    public void start() {
        final byte playerCount = this.view.askPlayerCount(Brikks.MAX_PLAYERS);
        if (playerCount == 0) {
            return;
        }

        final Level difficulty = this.view.askDifficulty();
        if (difficulty == null) {
            return;
        }

        final boolean duelMode;
        if (playerCount == 2) {
            duelMode = this.view.askDuel();
        } else {
            duelMode = false;
        }


        final String[] names = new String[playerCount];
        for (byte i = 0; i < playerCount; i++) {
            String name;
            do {
                name = this.view.askName((byte) (i + 1));
                if (name == null) {
                    return;
                }
            } while (this.saver.playerExists(name) && !this.view.askUseExistingPlayer(name));

            names[i] = name;
        }

        // Save
        this.players = new Player[playerCount];
        final PlayerSave[] playerSaves = this.saver.save(names, difficulty, duelMode);
        for (byte i = 0; i < playerCount; i++) {
            this.players[i] = new Player(playerSaves[i], names[i], (byte) names.length, difficulty);
        }
        this.firstChoice();

        this.firstSave = true;
        this.turn = (byte) (playerCount - 1);
        this.launch(difficulty, duelMode);
    }

    public void load() {
        final Level difficulty;
        final boolean duelMode;
        final byte turn;
        final Position turnChoice;
        {
            final List<SavedGame> variants = this.saver.load();
            final SavedGame choice = this.view.askChoiceSave(variants);
            if (choice == null) {
                return;
            }

            final LoadedGame loaded = this.saver.load(choice.ID(), this.blocksTable);

            this.players = loaded.players();
            this.matrixDie.cheat(loaded.matrixDie());
            difficulty = loaded.difficulty();
            duelMode = loaded.duel();
            turn = loaded.turn();
            turnChoice = loaded.choice();
        }

        this.firstSave = false;

        // Unfinished turn
        {
            final Loop loop = new Loop(turn, (byte) this.players.length);
            final Player player = this.players[turn];

            this.view.draw(player);

            player.setDuration();
            final TurnsResults result = player.turn(this.view, this, this.blocksTable, turnChoice);
            player.updateDuration();

            if (result.exit()) {
                this.players = null;
                this.turn = -1;
                return;
            } else if (result.giveUp()) {
                player.saveFinal();
                if (duelMode) {
                    this.end(difficulty, duelMode, loop.backcast());
                    this.players = null;
                    this.turn = -1;
                    return;
                }
            }

            if (duelMode && result.duelBonus() > 0) {
                if (!player.duelTurn(this.view, this.players[loop.forecast()], result.duelBonus())) {
                    this.end(difficulty, duelMode, this.turn);
                    this.players = null;
                    this.turn = -1;
                    return;
                }
            }

            this.turn = turn;
        }

        this.launch(difficulty, duelMode);
    }


    private void firstChoice() {
        final Position[] taken = new Position[this.players.length];

        final Loop looperRow = new Loop(BlocksTable.WIDTH);
        final Loop looperColumn = new Loop(BlocksTable.HEIGHT);
        for (byte i = 0; i < this.players.length; i++) {
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
            this.players[i].firstChoice(this.blocksTable.getBlock(firstChoice));
        }
    }

    private void launch(final Level difficulty, final boolean duelMode) {
        final RunsResults results;
        if (this.players.length == 1) {
            results = this.runSolo();
        } else {
            results = this.run(duelMode);
        }
        if (results.endGame()) {
            this.end(difficulty, duelMode, results.duelWinnerIndex());
        }

        this.players = null;
        this.turn = -1;
    }

    private RunsResults runSolo() {
        final Player player = this.players[0];
        this.turn = 0;

        do {
            this.view.draw(player);

            player.setDuration();
            final TurnsResults result = player.turn(this.view, this, this.blocksTable, this.matrixDie.roll());
            player.updateDuration();

            if (result.exit()) {
                return new RunsResults(false, (byte) -1);
            } else if (result.giveUp()) {
                player.saveFinal();
                break;
            }
        } while (player.isPlays());

        return new RunsResults(true, (byte) -1);
    }

    private RunsResults run(final boolean duelMode) {
        // TODO: fix
        final Loop loopingLoop = new Loop(this.turn, (byte) this.players.length);
        final Loop loop = new Loop((byte) this.players.length);

        boolean stillPlays;
        do {
            stillPlays = false;
            loop.setPosition(loopingLoop.goForward());

            boolean first = this.players.length != 1;
            for (; this.players.length == 1 || !loop.loopedForward(); loop.goForward()) {
                this.turn = loop.current();
                final Player player = this.players[this.turn];

                if (!player.isPlays()) {
                    continue;
                }

                stillPlays = true;
                this.view.draw(player);

                player.setDuration();
                final TurnsResults result;
                if (first) {
                    this.matrixDie.roll();
                    result = player.turn(this.view, this, this.blocksTable, this.matrixDie);
                    first = false;
                } else {
                    result = player.turn(this.view, this, this.blocksTable, this.matrixDie.get());
                }
                player.updateDuration();

                if (result.exit()) {
                    return new RunsResults(false, (byte) -1);
                } else if (result.giveUp()) {
                    player.saveFinal();
                    if (duelMode) {
                        return new RunsResults(true, loop.backcast());
                    }
                }

                if (duelMode && result.duelBonus() > 0) {
                    if (!player.duelTurn(this.view, this.players[loop.forecast()], result.duelBonus())) {
                        return new RunsResults(true, this.turn);
                    }
                }
            }
        } while (!stillPlays);

        return new RunsResults(true, (byte) -1);
    }

    private void end(final Level difficulty, final boolean duelMode, final byte winnerIndex) {
        for (final Player player : this.players) {
            player.saveFinal();
        }

        if (this.players.length == 1) {
            this.view.endSolo(this.players[0].name, this.players[0].calculateFinal(), difficulty);
        } else if (duelMode) {
            final Loop loop = new Loop(winnerIndex, (byte) 2);
            this.view.endDuel(this.players[loop.current()].name, this.players[loop.forecast()].name);
        } else {
            this.view.endStandard(this.players);
        }

        this.saver.dropSave();
    }


    @Override
    public void save(final Position choice) {
        System.out.println("Zberigajemo");
        if (this.firstSave) {
            this.saver.save(this.turn, choice, this.matrixDie.get());
            for (byte i = 0; i < this.players.length; i++) {
                this.players[i].save(i);
            }
            this.firstSave = false;
        } else {
            this.saver.update(this.turn, choice, this.matrixDie.get());
            for (final Player player : this.players) {
                player.update();
            }
        }
    }
}
