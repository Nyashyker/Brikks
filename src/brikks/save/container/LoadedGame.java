package brikks.save.container;

import brikks.Player;
import brikks.essentials.Position;
import brikks.essentials.enums.Level;

public record LoadedGame( Player[] players, Position matrixDie, byte turn, Position choice, Level difficulty, boolean duel ) {}
