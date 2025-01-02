package brikks.save.container;

import brikks.Player;
import brikks.essentials.Position;
import brikks.essentials.enums.Level;

public record LoadedGame( Player[] players, Position matrixDie, Level difficulty, boolean duel ) {}
