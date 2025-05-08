package brikks.save.container;

import java.time.LocalDateTime;


public record PlayerLeaderboard(
        String name,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        // Duration is pain to format!!!
        LocalDateTime duration,
        short score
) {}
