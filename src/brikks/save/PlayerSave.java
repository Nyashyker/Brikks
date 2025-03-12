package brikks.save;

import brikks.BlocksTable;
import brikks.Player;

public abstract class PlayerSave {
    protected final PlayerSave backup;
    protected boolean fail;


    protected PlayerSave(final PlayerSave backup) {
        this.backup = backup;
        this.fail = false;
    }


    ///        First save
    abstract public void save(final BlocksTable blocksTable, final Player player, final byte playerOrder);
    ///        Update
    abstract public void setDuration();
    abstract public void updateDuration();
    abstract public void update(final Player player);
    ///        End game
    abstract public void save(final short score);
}
