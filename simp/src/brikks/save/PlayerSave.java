package brikks.save;

import brikks.Player;
import brikks.essentials.*;
import brikks.logic.*;

public abstract class PlayerSave {
    abstract public void save(final Board board);
    abstract public void save(final Bombs bombs);
    abstract public void save(final BonusScore bonusScore);
    abstract public void save(final Energy energy);
    abstract public void save(final short finalScore);
    abstract public void save(final MatrixDice matrixDie);
    abstract public void save(final Player player);
    abstract public void save(final Position matrixDie);
    // TODO: called after final is save
    //private abstract void dropSave();
}
