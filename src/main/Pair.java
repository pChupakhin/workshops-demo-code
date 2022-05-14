package main;

public class Pair {

    private final Object first;
    private final Object second;

    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "class of \"first\" is " + first.getClass().getSimpleName()+ ",\tvalue of \"first\" is " + first
        + "\nclass of \"second\" is " + second.getClass().getSimpleName() + ",\tvalue of \"second\" is " + second;
    }

}
