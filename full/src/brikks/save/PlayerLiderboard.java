package brikks.save;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record PlayerLiderboard(
        String name,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        LocalTime duration,
        short score
) {}
