package brikks.save;


import brikks.BlocksTable;
import brikks.Player;


public class EmptyPlayerSave extends PlayerSave {
    public EmptyPlayerSave() {
        super(null);
    }

    @Override
    public void save(final BlocksTable blocksTable, final Player player, final byte playerOrder) {
    }

    @Override
    public void setDuration() {
    }

    @Override
    public void updateDuration() {
    }

    @Override
    public void update(final Player player, final BlocksTable blocksTable) {
    }

    @Override
    public void save(final short score) {
    }
}
