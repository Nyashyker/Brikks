package brikks.save;

import brikks.Player;
import brikks.essentials.MatrixDice;
import brikks.essentials.enums.Level;
import brikks.essentials.enums.Mode;

public record LoadedGame(
        Player[] players,
        MatrixDice matrixDie,
        Level difficulty,
        Mode mode
) {}
