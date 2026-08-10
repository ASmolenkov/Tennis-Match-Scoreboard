package io.github.asmolenkov.tennismatchscoreboard.model;

import lombok.Getter;

@Getter
public enum Point {
    ZERO("00"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    private final String displayValue;

    Point(String displayValue) {
        this.displayValue = displayValue;
    }

}
