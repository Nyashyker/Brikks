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
        if (this.rawRun(duelMode)) {
            this.end(difficulty, duelMode);
        }

        this.players = null;
        this.turn = -1;
    }


    private boolean rawRun(final boolean duelMode) {
        /*
         * -> false : exit
         */
        final Loop loopLoopPlayers = new Loop((byte) (this.players.length - 1), (byte) this.players.length);
        final Loop loopPlayers = new Loop((byte) this.players.length);

        boolean plays;
        do {
            plays = false;
            final Position roll;

            // TODO: fix first
            for (
                    loopPlayers.setPosition(loopLoopPlayers.goForward());
                    !plays || !loopPlayers.loopedForward();
                    loopPlayers.goForward()
            ) {
                final Player player = this.players[loopPlayers.current()];
                if (player.isPlays()) {
                    plays = true;
                } else {
                    continue;
                }

                final boolean turn;
                if (!plays) {
                    turn = this.rawTurn(player, null, duelMode ? this.players[loopPlayers.forecast()] : null);
                    roll = this.matrixDie.get();
                } else {
                    turn = this.rawTurn(player, roll, duelMode ? this.players[loopPlayers.forecast()] : null);
                }
                if (turn) {
                    return false;
                }
            }
        } while (plays);

        return true;
    }

    private boolean rawTurn(final Player player, final Position roll, final Player opponent) {
        /*
         * -> true : exit
         */
        this.view.draw(player);

        player.begin();
        final TurnsResults results;
        if (this.players.length == 1) {
            results = player.turn(this.view, this, this.blocksTable, this.matrixDie.roll());
        } else if (roll == null) {
            this.matrixDie.roll();
            results = player.turn(this.view, this, this.blocksTable, this.matrixDie);
        } else {
            results = player.turn(this.view, this, this.blocksTable, roll);
        }
        player.end();

        if (results.exit()) {
            return true;
        }

        if (opponent != null) {
            if (results.giveUp()) {
                player.saveFinal(false);
                opponent.saveFinal(true);

            } else if (results.duelBonus() > 0 && player.duelTurn(this.view, opponent, results.duelBonus())) {
                player.saveFinal(true);
                opponent.saveFinal(false);
            }

        } else if (results.giveUp()) {
            player.saveFinal();
        }

        return false;
    }

    private void end(final Level difficulty, final boolean duelMode) {
        if (this.players.length == 1) {
            this.view.endSolo(this.players[0].name, this.players[0].calculateFinal(), difficulty);

        } else if (duelMode) {
            final Player duelist0 = this.players[0], duelist1 = this.players[1];
            if (duelist0.calculateFinal() > duelist1.calculateFinal()) {
                this.view.endDuel(duelist0.name, duelist1.name);
            } else {
                this.view.endDuel(duelist1.name, duelist0.name);
            }

        } else {
            this.view.endStandard(this.players);
        }

        this.saver.dropSave();
    }


    @Override
    public void save(final Position choice) {
        if (this.firstSave) {
            this.firstSave = false;

            this.saver.save(this.turn, choice, this.matrixDie.get());
            for (byte i = 0; i < this.players.length; i++) {
                this.players[i].save(i);
            }

        } else {
            this.saver.update(this.turn, choice, this.matrixDie.get());
            for (final Player player : this.players) {
                player.update();
            }
        }
    }
}
