import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// In Run/Debug Configurations -> VM options type "-Duser.language=ua(for ukrainian)/ru(for russian)".
public class MainWithResources {
    
    // Time units
    private static final ChronoField[] CHRONOMETRICAL_UNITS;
    private static final Object[] LOCALIZED_CHRONOMETRICAL_UNITS_NAMES;
    
    // ChoiceFormat pattern
    private static final String STRING_TYPE_PATTERN_AS_CHOICE_FORMAT_CONSTRUCTOR_PARAMETER;
    private static final String SUBSTRING_TO_BE_REPLACED_WITH_CHRONOMETRICAL_UNIT_INDEX;
    private static final boolean REQUIRES_POSTFIXES_FOR_CHRONOMETRICAL_UNITS_VALUES;
    private static final String SUBSTRING_TO_BE_REPLACED_WITH_VALUE_POSTFIX_INDEX;
    private static final int[] INDICES_OF_CHRONOMETRICAL_UNITS_VALUES_WITH_SWITCHED_POSTFIX;
    private static final Object[] CHRONOMETRICAL_UNITS_VALUES_POSTFIXES;
    
    //Intermediate message
    private static final String FIRST_INTERMEDIATE_RESULT;
    private static final String SECOND_INTERMEDIATE_RESULT;
    
    //Result message
    private static final String FINAL_RESULT_MESSAGE;
    
    static {
        ResourceBundle localBundle = ResourceBundle.getBundle("resources");
        ResourceBundle globalBundle = ResourceBundle.getBundle("global_resources");
        
        CHRONOMETRICAL_UNITS = Stream.of(globalBundle.getString("chronometricalUnits").split("\\f"))
                .map(ChronoField::valueOf)
                .toArray(ChronoField[]::new);
        LOCALIZED_CHRONOMETRICAL_UNITS_NAMES = localBundle.getString("localizedChronometricalUnitsNames").split("\\f");
        
        STRING_TYPE_PATTERN_AS_CHOICE_FORMAT_CONSTRUCTOR_PARAMETER = localBundle.getString("pattern")
                .replaceFirst(Boolean.parseBoolean(localBundle.getString("requiresPostfixForChronometricalUnitsNames"))
                        ? globalBundle.getString("substringToBeReplacedWithNamePostfix") : "^$",
                        localBundle.getString("ChronometricalUnitsNamesPostfix"));
        SUBSTRING_TO_BE_REPLACED_WITH_CHRONOMETRICAL_UNIT_INDEX = globalBundle.getString("substringToBeReplacedWithChronometricalUnitIndex");
        REQUIRES_POSTFIXES_FOR_CHRONOMETRICAL_UNITS_VALUES
                = Boolean.parseBoolean(localBundle.getString("requiresPostfixesForChronometricalUnitsValues"));
        SUBSTRING_TO_BE_REPLACED_WITH_VALUE_POSTFIX_INDEX = globalBundle.getString("substringToBeReplacedWithValuePostfixIndex");
        INDICES_OF_CHRONOMETRICAL_UNITS_VALUES_WITH_SWITCHED_POSTFIX = Stream
                .of(localBundle.getString("indicesOfChronometricalUnitsValuesWithSwitchedPostfix").split("\\f"))
                .mapToInt(Integer::valueOf)
                .toArray();
        CHRONOMETRICAL_UNITS_VALUES_POSTFIXES = localBundle.getString("ChronometricalUnitsValuesPostfixes").split("\\f");
        
        FIRST_INTERMEDIATE_RESULT = localBundle.getString("firstIntermediateResult");
        SECOND_INTERMEDIATE_RESULT = localBundle.getString("secondIntermediateResult");
        
        FINAL_RESULT_MESSAGE = localBundle.getString("finalResultMessage");
    }
    
    public static void main(String[] args) {
        final Object[] elapsedTimelineChronometricalUnitsValues
                = DateTimeProvider.getChronometricalUnitsValuesOfElapsedTimeSinceRandomMoment(CHRONOMETRICAL_UNITS);
        final String startString = IntStream
                .iterate(0, i -> i + 1)
                .limit(elapsedTimelineChronometricalUnitsValues.length)
                .mapToObj(i -> "{" + i + "}")
                .collect(Collectors.joining());
        final MessageFormat formatter = new MessageFormat(startString);
        final ChoiceFormat[] formatsForEachNumberBetweenCurlyBracketsInStartString = IntStream
                .iterate(0, i -> i + 1)
                .limit(elapsedTimelineChronometricalUnitsValues.length)
                .mapToObj(i -> STRING_TYPE_PATTERN_AS_CHOICE_FORMAT_CONSTRUCTOR_PARAMETER
                        .replace(SUBSTRING_TO_BE_REPLACED_WITH_CHRONOMETRICAL_UNIT_INDEX, String.valueOf(i)))
                .map(ChoiceFormat::new)
                .toArray(ChoiceFormat[]::new);
        
        formatter.setFormats(formatsForEachNumberBetweenCurlyBracketsInStartString);
        String intermediateString = formatter.format(elapsedTimelineChronometricalUnitsValues).stripTrailing();
        
        System.out.printf("\n" + FIRST_INTERMEDIATE_RESULT + "\n", startString, intermediateString);
        
        if(REQUIRES_POSTFIXES_FOR_CHRONOMETRICAL_UNITS_VALUES) {
            
            {
                final String outdatedString = intermediateString;
                
                intermediateString = IntStream.iterate(0, i -> i + 1)
                        .limit(CHRONOMETRICAL_UNITS_VALUES_POSTFIXES.length)
                        .boxed()
                        .flatMap(i -> Stream.of(outdatedString.split(
                                        "(?<=\\{" + SUBSTRING_TO_BE_REPLACED_WITH_VALUE_POSTFIX_INDEX + "})\\B"))
                                .skip(i == 0 ? 0 : INDICES_OF_CHRONOMETRICAL_UNITS_VALUES_WITH_SWITCHED_POSTFIX[i - 1])
                                .limit(i == CHRONOMETRICAL_UNITS_VALUES_POSTFIXES.length - 1 ?
                                        elapsedTimelineChronometricalUnitsValues.length + 1
                                                - INDICES_OF_CHRONOMETRICAL_UNITS_VALUES_WITH_SWITCHED_POSTFIX[i - 1]
                                        : INDICES_OF_CHRONOMETRICAL_UNITS_VALUES_WITH_SWITCHED_POSTFIX[i])
                                .map(patternSubstring -> patternSubstring
                                        .replace(SUBSTRING_TO_BE_REPLACED_WITH_VALUE_POSTFIX_INDEX, String.valueOf(i))))
                        .collect(Collectors.joining());
            }
            
            final String outdatedString = intermediateString;
            intermediateString = MessageFormat.format(intermediateString, CHRONOMETRICAL_UNITS_VALUES_POSTFIXES);
            
            System.out.printf(SECOND_INTERMEDIATE_RESULT + "\n", intermediateString, outdatedString);
        }
        
        final String finalString = MessageFormat.format(intermediateString, LOCALIZED_CHRONOMETRICAL_UNITS_NAMES);
        final String from = DateTimeProvider.getFrom().format(DateTimeFormatter.ofPattern("kk:mm:ss dd.MM.y"));
        final String to = DateTimeProvider.getTo().format(DateTimeFormatter.ofPattern("kk:mm:ss dd.MM.y"));
        
        System.out.printf("\n" + FINAL_RESULT_MESSAGE + "\n", finalString, from, to);
    }
    
}
