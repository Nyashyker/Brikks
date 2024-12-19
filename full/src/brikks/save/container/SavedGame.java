package brikks.save.container;

import java.time.LocalDateTime;

public record SavedGame(int ID, String[] playerNames, LocalDateTime startTime) {}
