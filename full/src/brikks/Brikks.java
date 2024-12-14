package brikks;

import brikks.essentials.Block;
import brikks.essentials.MatrixDice;
import brikks.save.BoardPointCalculation;
import brikks.save.Save;
import brikks.view.View;

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
        this.matrixDie = new MatrixDice(BlocksTable.width, BlocksTable.height);

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
                case EXIT, null, default -> System.exit(0);
            };
        }
    }

    public void liderboard() {
        this.view.liderboard(this.save.liderboard());
    }

    public void start() {
        for (String name : this.view.askNames()) {
            this.players.add(new Player())
        }
    }
}
