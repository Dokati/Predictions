package Exceptions;

public class NotNumericValueException extends IllegalArgumentException {
    public NotNumericValueException() {
        super("Object is NOT numeric type.");
    }

}
