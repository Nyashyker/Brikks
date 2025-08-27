package brikks.save.container;

import java.time.Duration;
import java.time.LocalDateTime;


public record PlayerLeaderboard(
        String name,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Duration duration,
        short score
) {}
