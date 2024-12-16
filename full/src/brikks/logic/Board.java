package brikks.logic;

import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.board.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final byte WIDTH = 10;
    public static final byte HEIGHT = 11;

    private final List<PlacedBlock> placed;

    private final BonusScore bonusScore;
    private final Energy energy;

    private final BonusEnergyBoard bonusEnergy;
    private final UsedBoard used;

    public Board(final BonusScore bonusScore, final Energy energy, final Level difficulty) {
        this.placed = new ArrayList<PlacedBlock>();

        this.bonusScore = bonusScore;
        this.energy = energy;

        this.bonusEnergy = switch (difficulty) {
            case ONE -> new BonusEnergyBoardD1(Board.WIDTH, Board.HEIGHT);
            case TWO -> new BonusEnergyBoardD2(Board.WIDTH, Board.HEIGHT);
            case THREE -> new BonusEnergyBoardD3(Board.WIDTH, Board.HEIGHT);
            case FOUR -> new BonusEnergyBoardD4(Board.WIDTH, Board.HEIGHT);
        };
        this.used = new UsedBoard(Board.WIDTH, Board.HEIGHT);
    }
}
