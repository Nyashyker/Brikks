package brikks.view.container;

import brikks.essentials.enums.Level;

public record LeaderboardOptions(
        int count /* = 12; */,
        String name,
        Level difficulty,
        byte players /* = 0; // disabled */,
        boolean duel /* = false; // Overrides `players` to 2 if enabled */,
        boolean reverse /* = false; */
) {}
