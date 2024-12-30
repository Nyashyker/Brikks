package brikks.view;

import brikks.Player;
import brikks.essentials.Position;

public interface DuelAsk {
    Position askPlacingMiniblock(final Player opponent, final Position[] variants);
}
