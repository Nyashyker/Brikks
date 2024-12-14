package brikks.save;

import java.time.LocalDateTime;

public record SavedGame(int ID, String[] playerNames, LocalDateTime startTime) {}
