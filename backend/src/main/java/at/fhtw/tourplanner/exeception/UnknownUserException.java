package at.fhtw.tourplanner.exeception;

public class UnknownUserException extends RuntimeException {
    public UnknownUserException(String message) {
        super(message);
    }
    public UnknownUserException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public UnknownUserException(Throwable throwable) {
        super(throwable);
    }
}
