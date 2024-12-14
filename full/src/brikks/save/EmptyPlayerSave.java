package brikks.save;

import brikks.Player;
import brikks.essentials.*;
import brikks.logic.*;

public class EmptyPlayerSave extends PlayerSave {
    public EmptyPlayerSave() {}

    @Override
    public void save(final Board board) {}
    @Override
    public void save(final Bombs bombs) {}
    @Override
    public void save(final BonusScore bonusScore) {}
    @Override
    public void save(final Energy energy) {}
    @Override
    public void save(final short finalScore) {}
    @Override
    public void save(final MatrixDice matrixDie) {}
    @Override
    public void save(final Player player) {}
    @Override
    public void save(final byte row, final byte column) {}
}
