package io.github.asmolenkov.tennismatchscoreboard.service;

import java.util.function.Supplier;

public interface TransactionManager {
    <T> T executeInTransaction(Supplier<T> action);
}
