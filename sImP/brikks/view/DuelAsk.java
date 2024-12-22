package brikks.view;

import brikks.logic.Board;
import brikks.essentials.*;
import brikks.view.enums.*;

public interface DuelAsk {
    Position askPlacingMiniblock(Board opponentsBoard, Position[] variants);
}
