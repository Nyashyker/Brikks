package brikks.save.container;

import java.time.LocalDateTime;
import java.time.Duration;

public record PlayerLiderboard(
        String name,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Duration duration,
        short score
) {}
