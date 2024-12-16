package brikks;

import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.save.*;
import brikks.view.*;

import java.util.List;
import java.util.ArrayList;

public class Brikks {
    private BlocksTable blocksTable;
    private MatrixDice matrixDie;

    private final Save save;
    private final View view;

    private BoardPointCalculation boardCalculator;
    private List<Player> players;

    public Brikks(View view, Save save, Block[][] blocksTable) {
        this.blocksTable = new BlocksTable(blocksTable);
        this.matrixDie = new MatrixDice(BlocksTable.WIDTH, BlocksTable.HEIGHT);

        this.save = save;
        this.view = view;

        this.players = new ArrayList<Player>();
    }

    public void menu() {
        while (true) {
            switch (this.view.menu()) {
                case NEW_GAME -> this.start();
                case LOAD -> this.load();
                case LIDERBOARD -> this.liderboard();
                case EXIT -> System.exit(0);
            };
        }
    }

    public void liderboard() {
        this.view.liderboard(this.save.liderboard());
    }

    public void start() {
        String[] name$s = this.view.askNames();
        Level difficulty = this.view.askDifficulty();

        for (String name : name$s) {
            this.players.add(new Player(this.save.createPlayerSave(name), name, (byte) name$s.length, difficulty));
        }

/*
        switch (this.view.askMode()) {
            case SOLO -> this.runSolo();
            case STANDARD -> this.runStandard();
            case DUEL -> this.runDuel();
        }
*/
    }

    public void load() {
        // TODO: The load functional
    }
}
