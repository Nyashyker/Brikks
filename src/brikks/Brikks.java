package brikks;

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

import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

        final boolean duelMode;
        if (playerCount == 2) {
            duelMode = this.view.askDuel();
        } else {
            duelMode = false;
        }

        final Level difficulty = this.view.askDifficulty();
        if (difficulty == null) {
            return;
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
        {
            this.players = new Player[playerCount];
            final PlayerSave[] playerSaves = this.saver.save(names, difficulty, duelMode);
            for (byte i = 0; i < playerCount; i++) {
                this.players[i] = new Player(playerSaves[i], names[i], (byte) names.length, difficulty);
            }
        }

        // First choice
        {
            final Set<Position> taken = new HashSet<>(playerCount);
            final Loop looperRow = new Loop(BlocksTable.WIDTH);
            final Loop looperColumn = new Loop(BlocksTable.HEIGHT);

            for (final Player player : this.players) {
                final Position firstChoice = this.matrixDie.roll();

                // Ensure that there are no duplicates
                looperRow.setPosition(firstChoice.getX());
                looperColumn.setPosition(firstChoice.getY());

                final byte takenSize = (byte) taken.size();
                taken.add(firstChoice);
                while (takenSize == taken.size()) {
                    firstChoice.setX(looperRow.goForward());
                    if (looperRow.loopedForward()) {
                        firstChoice.setY(looperColumn.goForward());
                    }

                    taken.add(firstChoice);
                }

                player.firstChoice(this.blocksTable.getBlock(firstChoice));
            }
        }

        this.firstSave = true;
        this.turn = 0;
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

        // TODO: make the saved turn work

        this.firstSave = false;
        this.launch(difficulty, duelMode);
    }


    private void launch(final Level difficulty, final boolean duelMode) {
        final byte duelWinner;
        final boolean gameOver;

        if (this.players.length == 1) {
            duelWinner = -1;
            gameOver = this.runSolo();

        } else if (duelMode) {
            duelWinner = this.runDuel();
            // Should always be true
            gameOver = duelWinner != -1;

        } else {
            duelWinner = -1;
            gameOver = this.run();
        }

        if (gameOver) {
            this.end(difficulty, duelMode, duelWinner);
        }

        this.players = null;
        this.turn = -1;
    }

    private boolean runSolo() {
        final Player player = this.players[0];

        do {
            this.view.draw(player);

            player.begin();
            final TurnsResults result = player.turn(this.view, this, this.blocksTable, this.matrixDie.roll());
            player.end();

            if (result.exit()) {
                return false;
            } else if (result.giveUp()) {
                player.saveFinal();
                break;
            }
        } while (player.isPlays());

        return true;
    }

    private byte runDuel() {
        final Loop loopingLoop = new Loop(this.turn, (byte) this.players.length);
        final Loop loop = new Loop((byte) this.players.length);

        while (this.players[0].isPlays() && this.players[1].isPlays()) {
            loop.setPosition(loopingLoop.goForward());

            boolean first = true;
            for (; !loop.loopedForward(); loop.goForward()) {
                this.turn = loop.current();
                final Player player = this.players[this.turn];

                this.view.draw(player);

                player.begin();
                final TurnsResults result;
                if (first) {
                    first = false;
                    this.matrixDie.roll();
                    result = player.turn(this.view, this, this.blocksTable, this.matrixDie);
                } else {
                    result = player.turn(this.view, this, this.blocksTable, this.matrixDie.get());
                }
                player.end();

                if (result.exit() || result.giveUp()) {
                    return loop.backcast();
                } else if (result.duelBonus() > 0 &&
                        player.duelTurn(this.view, this.players[loop.forecast()], result.duelBonus())) {
                    return loop.current();
                }
            }
        }
        // Should never be reached
        return -1;
    }

    private boolean run() {
        final Loop loopingLoop = new Loop(this.turn, (byte) this.players.length);
        final Loop loop = new Loop((byte) this.players.length);

        boolean stillPlays;
        do {
            stillPlays = false;
            loop.setPosition(loopingLoop.goForward());

            boolean first = true;
            for (; !loop.loopedForward(); loop.goForward()) {
                this.turn = loop.current();
                final Player player = this.players[this.turn];

                if (!player.isPlays()) {
                    continue;
                }
                stillPlays = true;

                this.view.draw(player);

                player.begin();
                final TurnsResults result;
                if (first) {
                    first = false;
                    this.matrixDie.roll();
                    result = player.turn(this.view, this, this.blocksTable, this.matrixDie);
                } else {
                    result = player.turn(this.view, this, this.blocksTable, this.matrixDie.get());
                }
                player.end();

                if (result.exit()) {
                    return false;
                } else if (result.giveUp()) {
                    player.saveFinal();
                }
            }
        } while (!stillPlays);

        return true;
    }

    private void end(final Level difficulty, final boolean duelMode, final byte winnerIndex) {
        for (final Player player : this.players) {
            player.saveFinal();
        }

        if (this.players.length == 1) {
            this.view.endSolo(this.players[0].name, this.players[0].calculateFinal(), difficulty);
        } else if (duelMode) {
            this.view.endDuel(this.players[winnerIndex].name, this.players[this.nextDuelist(winnerIndex)].name);
        } else {
            this.view.endStandard(this.players);
        }

        this.saver.dropSave();
    }


    private byte nextDuelist(final byte current) {
        // 0 -> 1 || 1 -> 0
        return (byte) (current ^ 1);
    }

    @Override
    public void save(final Position choice) {
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
