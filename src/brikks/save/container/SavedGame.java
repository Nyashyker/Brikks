package brikks.save.container;

import java.time.LocalDateTime;
import java.util.List;


public record SavedGame(int ID, List<String> playerNames, LocalDateTime startTime) {
}
