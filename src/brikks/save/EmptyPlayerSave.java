package brikks.save;


import brikks.BlocksTable;
import brikks.Player;


public class EmptyPlayerSave extends PlayerSave {
    public EmptyPlayerSave() {
        super(null);
    }


    ///        First save
    @Override
    public void save(final BlocksTable blocksTable, final Player player, final byte playerOrder) {}

    ///        Update
    @Override
    public void setDuration() {}

    @Override
    public void updateDuration() {}

    @Override
    public void update(final Player player, final BlocksTable blocksTable) {}

    ///        End game
    @Override
    public void save(final short score) {}
}
