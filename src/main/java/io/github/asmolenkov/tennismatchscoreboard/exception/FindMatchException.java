package io.github.asmolenkov.tennismatchscoreboard.exception;

public class FindMatchException extends RuntimeException {

    // TODO: Оригинальное исключение (Exception e) не передаётся в конструктор супер-класса и молча проглатывается
    public FindMatchException(String message, Exception e){
        super(message, e);}

    public FindMatchException(String message){
        super(message);}
}
