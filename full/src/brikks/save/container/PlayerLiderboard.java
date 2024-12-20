package brikks.save.container;

import java.time.LocalDateTime;
import java.time.Duration;

public record PlayerLiderboard(
        String name,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Duration duration,
        short score
) {
    @Override
    public String toString() {
        return String.format("%s - (%s - %s) %s - %d",
                this.name, this.startDateTime, this.endDateTime, this.duration, this.score);
    }
}
