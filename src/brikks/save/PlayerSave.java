package brikks.save;

import brikks.Player;

public interface PlayerSave {
    ///        First save
    // TODO: !!! USE !!!
    void save(final Player player, final byte playerOrder);
    ///        Update
    void setDuration();
    void updateDuration();
    void update(final Player player);
    ///        End game
    void save(final short score);
}
