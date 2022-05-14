package main;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static class Examples {
        private static final int FUNCTION_EXAMPLE = 1;
        private static final int STREAM_SUPPLIER_AND_CONSUMER_EXAMPLE = 2;
        private static final int BI_CONSUMER_EXAMPLE = 3;
        private static final int BI_PREDICATE_EXAMPLE = 4;
        private static final int BINARY_OPERATOR_EXAMPLE = 5;
        private static final int CUSTOM_FUNCTIONAL_INTERFACE_EXAMPLE = 6;
        private static final int CONCURRENT_FUNCTIONAL_INTERFACE_EXAMPLE = 7;
    }

    public static void main(String[] args) throws Exception {
        switch (0) {
            case Examples.FUNCTION_EXAMPLE -> functionExample();
            case Examples.STREAM_SUPPLIER_AND_CONSUMER_EXAMPLE -> streamSupplierAndConsumerExample();
            case Examples.BI_CONSUMER_EXAMPLE -> biConsumerExample();
            case Examples.BI_PREDICATE_EXAMPLE -> biPredicateExample();
            case Examples.BINARY_OPERATOR_EXAMPLE -> binaryOperatorExample();
            case Examples.CUSTOM_FUNCTIONAL_INTERFACE_EXAMPLE -> customFunctionalInterfacesExample();
            case Examples.CONCURRENT_FUNCTIONAL_INTERFACE_EXAMPLE -> concurrentFunctionalInterfacesExample();
        }
    }

    private static void functionExample() {
        /*ToDoubleFunction<String>*/Function<String, Double> stringToDoubleFunction = str -> Math.pow(Integer.parseInt(str), 2);
        double powerOfTwo = stringToDoubleFunction.apply/*AsDouble*/("2");
        System.out.println("power of two = " + powerOfTwo);
    }

    private static void streamSupplierAndConsumerExample() {
        Supplier<Stream<Integer>> streamSupplier = () -> Stream.of(1, 2, 3);
        Consumer<Stream<Integer>> streamConsumer = streamOfIntegers -> streamOfIntegers.forEach(i -> System.out.println(i + " = " + i));
        streamConsumer.accept(streamSupplier.get());
        streamConsumer = streamOfIntegers -> streamOfIntegers.forEach(i -> System.out.println(i + " * "+ i + " = " + i * i + "\t"));
        streamConsumer.accept(streamSupplier.get());

//        Stream<Integer> stream = Stream.of(1, 2, 3);
//        stream.forEach(System.out::println);
//        stream.forEach(System.out::println);

    }

    private static void biConsumerExample() {
        BiConsumer<Integer, Integer> biConsumerOfIntegers
                = (i, j) -> IntStream.rangeClosed(i, j).forEach(System.out::println);
        biConsumerOfIntegers.accept(1, 20);

//        Consumer<List<String>> consumerOfString = list -> System.out.println(list);
//        consumerOfString.andThen(list -> list.add("first string"))
//                .andThen(list -> list.add("second string"))
//                .andThen(list -> list.forEach(System.out::println))
//                .accept(new ArrayList<>(List.of("third string")));
    }

    private static void biPredicateExample() {
        BiPredicate<Integer, BiPredicate<Integer, ?>> biPredicate = (i, thisBiPredicate) -> i == 0;
        boolean isTrue = biPredicate
                .and((i, thisBiPredicate) -> thisBiPredicate instanceof BiPredicate<Integer, ?>)
                //.negate()
                .test(0, biPredicate);
        System.out.println(isTrue);
    }

    private static void binaryOperatorExample() {
        BinaryOperator<List<Integer>> listOfIntegerBinaryOperator = (list1, list2) -> {
            List<Integer> result = new ArrayList<>();
            result.addAll(list1);
            result.addAll(list2);
            return result;
        };

        int sum = listOfIntegerBinaryOperator
                .andThen(list -> list.stream()
                        .reduce(0,(res, i) -> res + i))
                .apply(List.of(1, 2, 3), List.of(18, 19, 20));

        System.out.println("sum = " + sum);
    }

    private static void customFunctionalInterfacesExample() {
        PairExtension pairExtension = (i, str) -> new Pair(i, str);
        Pair pair = pairExtension.wrap(1, "2");
        System.out.println("pair of:\n" + pair);
    }

    private static void concurrentFunctionalInterfacesExample() throws Exception {
        Runnable runnable = () -> System.out.println("value printed in runnable.run() = " + 1);
        runnable.run();
        //is equal to
        System.out.println("value printed in this method = " + 1);

        System.out.println("-".repeat(85));

        {
            Callable<Integer> callable = () -> 2;
            int i = callable.call();
            System.out.println("value received from callable.call() = " + i);
        }
        //is equal to
        {
            int i = 2;
            System.out.println("value received from this method = " + i);
        }
    }

}
