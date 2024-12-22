package brikks.save.container;

import brikks.Player;
import brikks.essentials.Position;

public record LoadedGame( Player[] players, Position matrixDie, boolean duel ) {}
