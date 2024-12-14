package brikks;

import brikks.save.PlayerSave;
import brikks.essentials.enums.Level;
import brikks.logic.*;

public class Player {
    public final String name;
    private boolean plays;

    private final PlayerSave saver;
    private final BonusScore bonusScore;
    private final Energy energy;
    private final Bombs bombs;
    private final Board board;

    public Player(PlayerSave saver, String name, byte playerCount, Level difficulty) {
        this.saver = saver;
        this.name = name;
        this.plays = true;
        this.bonusScore = new BonusScore();
        this.energy = new Energy(this.bonusScore, playerCount);
        this.bombs = new Bombs();
        this.board = new Board(this.bonusScore, this.energy, difficulty);
    }
}
