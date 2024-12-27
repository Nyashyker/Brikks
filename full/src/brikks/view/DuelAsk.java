package brikks.view;

import brikks.Player;
import brikks.logic.Board;
import brikks.essentials.*;
import brikks.view.enums.*;

public interface DuelAsk {
    Position askPlacingMiniblock(final Player opponent, final Position[] variants);
}
