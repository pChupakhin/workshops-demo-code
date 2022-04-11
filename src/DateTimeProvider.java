import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

import java.util.Random;

import java.util.stream.Stream;

public class DateTimeProvider {
    
    private static final Random random = new Random();
    
    private static final LocalDateTime to = LocalDateTime.now();
    private static final LocalDateTime from = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(random.nextLong(to.toInstant(ZoneOffset.UTC).getEpochSecond() + 1)),
                        ZoneOffset.UTC)
                .minusYears(1970).plusYears(random.nextLong(1971));
    
    private DateTimeProvider(){}
    
    public static LocalDateTime getFrom() {
        return from;
    }
    
    public static LocalDateTime getTo() {
        return to;
    }
    
    public static Long[] getChronometricalUnitsValuesOfElapsedTimeSinceRandomMoment(final ChronoField[] dateTimeUnits) {
        final LocalDateTime elapsedTimelineInDateTimeFormat = to.minusSeconds(from.toInstant(ZoneOffset.UTC).getEpochSecond())
                .minusYears(1970).minusMonths(1).minusDays(1);
        
        return Stream.of(dateTimeUnits)
                .map(elapsedTimelineInDateTimeFormat::getLong)
                .toArray(Long[]::new);
    }
    
}
