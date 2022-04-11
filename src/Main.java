import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatter;

import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import java.text.MessageFormat;
import java.text.ChoiceFormat;

public class Main {
    
    private static final String INDEX = "TO-BE-REPLACED-WITH (int)i";
    
    private static final ChronoField[] CHRONOMETRICAL_UNITS = {
            ChronoField.YEAR,
            ChronoField.MONTH_OF_YEAR,
            ChronoField.DAY_OF_MONTH,
            ChronoField.HOUR_OF_DAY,
            ChronoField.MINUTE_OF_HOUR,
            ChronoField.SECOND_OF_MINUTE
    };
    
    public static void main(String[] args) {
        final Object[] elapsedTimelineChronometricalUnitsValues =
                DateTimeProvider.getChronometricalUnitsValuesOfElapsedTimeSinceRandomMoment(CHRONOMETRICAL_UNITS);
        
        final String startString = IntStream
                .iterate(0, i -> i + 1)
                .limit(CHRONOMETRICAL_UNITS.length)
                .mapToObj(i -> "{" + i + "}")
                .collect(Collectors.joining()); // "{0}{1}{2}{3}{4}{5}"
        
        final MessageFormat formatter = new MessageFormat(startString);
        
        final double[] limits = {0, 1, 2};
        final String[] parts = new String[]{
                "",                                                     // limits[0] = 0; if {i} >= 0 then parts[0]
                "{" + INDEX + ",number,#} " + "'{'" + INDEX + "'}",     // limits[1] = 1; if {i} >= 1 then parts[1]
                "{" + INDEX + ",number,#} " + "'{'" + INDEX + "'}'s"    // limits[2] = 2; if {i} >= 2 then parts[2]
        };
        
        final ChoiceFormat[] formatsForEachNumberBetweenCurlyBracketsInStartString
                = IntStream .iterate(0, i -> i + 1)
                .limit(CHRONOMETRICAL_UNITS.length)
                .mapToObj(i -> Stream.of(parts)
                        .map(part -> part
                                .replace("TO-BE-REPLACED-WITH (int)i", String.valueOf(i))
                                .replaceFirst("(?<!^)$", i == CHRONOMETRICAL_UNITS.length - 1 ? "" : " "))
                        .toArray(String[]::new))
                .map(updatedParts -> new ChoiceFormat(limits, updatedParts))
                .toArray(ChoiceFormat[]::new);
    
        formatter.setFormats(formatsForEachNumberBetweenCurlyBracketsInStartString);
        
        final String intermediateString = formatter.format(elapsedTimelineChronometricalUnitsValues);
        System.out.println("\nthe result of the first use of MessageFormatter: "
                + startString + " -> " + intermediateString + "\n");
        
        final Object[] chronometricalUnitsNames = Stream.of(CHRONOMETRICAL_UNITS)
                .map(timeUnits -> timeUnits.name().replaceFirst("_.+", "").toLowerCase())
                .toArray();
        
        final String finalString = MessageFormat.format(intermediateString, chronometricalUnitsNames);
        final String from = DateTimeProvider.getFrom().format(DateTimeFormatter.ofPattern("kk:mm:ss dd.MM.y"));
        final String to = DateTimeProvider.getTo().format(DateTimeFormatter.ofPattern("kk:mm:ss dd.MM.y"));
        
        System.out.printf("%s passed\nfrom %s\ntill %s\n", finalString, from, to);
    }
    
}
