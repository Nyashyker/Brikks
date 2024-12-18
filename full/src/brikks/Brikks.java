package brikks;

import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.save.*;
import brikks.view.*;
import circle_loop.ByteLoop;

import java.util.Random;

public class Brikks {
    public static final byte MAX_PLAYERS = 4;

    private static final Random rand = new Random();


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

        // TODO: dodaj persxj vybir

        switch (playerCount) {
            case 1 -> this.runSolo(players[0]);
            case 2 -> {
                switch (this.view.askMode()) {
                    case STANDARD -> this.runStandard(players);
                    case DUEL -> this.runDuel(players);
                }
            }
            default -> this.runStandard(players);
        }
    }

    public void load() {
        // TODO: The load functional
    }


    private void runSolo(final Player player) {
        while (player.isPlays()) {
            this.view.draw(player);
            TurnsResults result = player.turn(this.view, this.blocksTable, this.matrixDie.roll());
            if (result.exit()) {
                return;
            }
        }

        this.view.endSolo(this.save.getRanks(), player.calculateFinal());
    }

    private void runStandard(final Player[] players) {
        ByteLoop loop = new ByteLoop((byte) players.length);
        byte stillPlays;
        do {
            loop.setPosition((byte) Brikks.rand.nextInt(players.length));
            stillPlays = (byte) players.length;

            boolean first = true;
            final Position roll = this.matrixDie.roll();
            for (; loop.loopedForward(); loop.goForward()) {
                final Player player = players[loop.current()];

                if (!player.isPlays()) {
                    stillPlays--;
                    continue;
                }

                this.view.draw(player);

                TurnsResults result;
                if (first) {
                    result = players[loop.current()].turn(this.view, this.blocksTable, this.matrixDie);
                    first = false;
                } else {
                    result = player.turn(this.view, this.blocksTable, new Position(roll));
                }

                if (result.exit()) {
                    player.stop();
                }
            }
        } while (stillPlays != 0);

        this.view.endStandard(players);
    }

    private void runDuel(final Player[] players) {
        byte winner = -1;

        ByteLoop loop = new ByteLoop((byte) players.length);
        while (winner == -1) {
            loop.setPosition((byte) Brikks.rand.nextInt(players.length));

            boolean first = true;
            final Position roll = this.matrixDie.roll();
            for (; loop.loopedForward(); loop.goForward()) {
                final Player player = players[loop.current()];

                if (!player.isPlays()) {
                    winner = loop.backcast();
                    break;
                }

                this.view.draw(player);

                TurnsResults result;
                if (first) {
                    result = players[loop.current()].turn(this.view, this.blocksTable, this.matrixDie);
                    first = false;
                } else {
                    result = player.turn(this.view, this.blocksTable, new Position(roll));
                }

                if (result.exit()) {
                    winner = loop.forecast();
                    break;
                }

                if (player.duelTurn(this.view, players[loop.forecast()].getBoard(), result.duelBonus())) {
                    winner = loop.current();
                    break;
                }
            }
        }

        loop.setPosition(winner);
        this.view.endDuel(players[loop.current()], players[loop.goForward()]);
    }
}
