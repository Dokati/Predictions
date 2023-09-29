package Exceptions;

public class NotSameTypeException extends IllegalArgumentException {
    public NotSameTypeException() {
        super("Objects are not of the same type.");
    }
}