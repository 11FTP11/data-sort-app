package ru.aston.teamproject.contexts;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

public class FillContext<E extends Comparable<E>> implements Context<E> {
    private Strategy<E> strategy;

    @Override
    public void setStrategy(Strategy<E> strategy) {
        this.strategy = strategy;
    }

    @Override
    public void handle(EntityStorage<E> storage) {
        if (strategy == null) {
            throw new IllegalStateException("Стратегия заполнения не установлена!");
        }
        strategy.execute(storage);
    }
}