package main;

@FunctionalInterface
public interface PairExtension {

    Pair wrap(Object first, Object second);

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);

}
