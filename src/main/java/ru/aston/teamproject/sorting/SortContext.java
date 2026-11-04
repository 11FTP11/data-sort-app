package ru.aston.teamproject.sorting;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.Context;
import ru.aston.teamproject.strategies.Strategy;

public class SortContext<E extends Comparable<E>> implements Context<E> {
    private final String ERROR_MESSAGE = "[Exception]: Стратегия сортировки не установлена.";

    private Strategy<E> strategy;

    @Override
    public void setStrategy(Strategy<E> strategy) {
        this.strategy = strategy;
    }

    @Override
    public void handle(EntityStorage<E> data) {
        if (strategy == null) {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
        strategy.execute(data);
    }
}
