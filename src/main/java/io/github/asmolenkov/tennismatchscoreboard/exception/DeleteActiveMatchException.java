package io.github.asmolenkov.tennismatchscoreboard.exception;

public class DeleteActiveMatchException extends RuntimeException {
    public DeleteActiveMatchException(String message) {
        super(message);
    }
}
