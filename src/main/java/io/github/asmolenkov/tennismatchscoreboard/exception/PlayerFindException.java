package io.github.asmolenkov.tennismatchscoreboard.exception;

public class PlayerFindException extends RuntimeException {
    public PlayerFindException(String message, Exception e) {
        super(message, e);
    }
}
