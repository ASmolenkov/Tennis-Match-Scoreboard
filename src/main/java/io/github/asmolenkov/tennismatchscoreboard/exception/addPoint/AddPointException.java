package io.github.asmolenkov.tennismatchscoreboard.exception.addPoint;

public class AddPointException extends RuntimeException {
    public AddPointException(String message) {
        super(message);
    }

    public AddPointException(String message, Exception e) {
        super(message,e);
    }
}
