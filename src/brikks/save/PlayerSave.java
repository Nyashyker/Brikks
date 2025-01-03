package brikks.save;

import brikks.Player;

public interface PlayerSave {
    void save(final Player player, final byte playerOrder);
    void update(final Player player);
    void save(final short score);
}
