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
    private byte turnRotation;
    private boolean firstSave;


    public Brikks(final View view, final Save saver) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        if (saver == null) {
            throw new IllegalArgumentException("save cannot be null");
        }

        this.blocksTable = new BlocksTable();
        this.matrixDie = new MatrixDice(BlocksTable.WIDTH, BlocksTable.HEIGHT);

        this.saver = saver;
        this.view = view;
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

        // Players creation
        {
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
        this.turn = (byte) (playerCount - 1);
        this.turnRotation = 0;

        if (this.rawRun(duelMode)) {
            this.end(difficulty, duelMode);
        }
    }

    public void load() {
        final Level difficulty;
        final boolean duelMode;
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
            this.turn = loaded.turn();
            this.turnRotation = loaded.turnRotation();
            turnChoice = loaded.choice();
        }
        this.firstSave = false;

        // Perform the turn on which was saved
        if (this.rawTurn(this.turn, turnChoice, duelMode)) {
            return;
        }

        if (this.rawRun(duelMode)) {
            this.end(difficulty, duelMode);
        }
    }


    private boolean rawRun(final boolean duelMode) {
        /*
         * -> false : exit
         */
        final Loop loopLoopPlayers = new Loop(this.turn, (byte) this.players.length);
        final Loop loopPlayers = new Loop((byte) this.players.length);

        boolean plays;
        do {
            plays = false;
            Position roll = null;

            this.turnRotation = loopLoopPlayers.goForward();
            loopPlayers.setPosition(this.turnRotation);
            do {
                if (this.players[loopPlayers.current()].isPlays()) {
                    plays = true;
                } else {
                    continue;
                }

                final boolean turn = this.rawTurn(loopPlayers.current(), roll, duelMode);
                if (roll == null) {
                    roll = this.matrixDie.get();
                }

                if (turn) {
                    return false;
                }
                loopPlayers.goForward();
            } while (!loopPlayers.loopedForward() && this.players.length != 1);
        } while (plays);

        return true;
    }

    private boolean rawTurn(final byte turn, final Position roll, final boolean duelMode) {
        // turn ^ 1 => 1 -> 0 || 0 -> 1
        return this.rawTurn(this.players[turn], roll, duelMode ? this.players[turn ^ 1] : null);
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

            this.saver.save(this.turn, this.turnRotation, choice, this.matrixDie.get());
            for (byte i = 0; i < this.players.length; i++) {
                this.players[i].save(this.blocksTable, i);
            }

        } else {
            this.saver.update(this.turn, this.turnRotation, choice, this.matrixDie.get());
            for (final Player player : this.players) {
                player.update(this.blocksTable);
            }
        }
    }
}
