package Exceptions;

public class OutOfRangeException extends Exception{

    public OutOfRangeException(Float from, Float to) {
        super("Please enter an Float value between " + from + " and "+ to);
    }

    public OutOfRangeException(Integer from,Integer to) {
        super("Please enter an integer value between " + from + " and " + to);
    }
}
