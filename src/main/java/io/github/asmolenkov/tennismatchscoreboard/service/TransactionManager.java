package io.github.asmolenkov.tennismatchscoreboard.service;

import io.github.asmolenkov.tennismatchscoreboard.entity.Match;

import java.util.function.Supplier;

public interface TransactionManager {
    <T> T executeInTransaction(Supplier<T> action);
    Match executeInTransaction(Runnable action);
}
