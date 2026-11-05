package ru.aston.teamproject.strategies;

import ru.aston.teamproject.collections.EntityStorage;

import java.util.Objects;

public class EntityContext<E extends Comparable<E>> implements Context<E> {
    private Strategy<E> strategy;

    @Override
    public void setStrategy(Strategy<E> strategy) {
        this.strategy = strategy;
    }

    @Override
    public void handle(EntityStorage<E> storage) {
        Objects.requireNonNull(this.strategy, "Cтратегия не была установлена в контекст");
        strategy.execute(storage);
    }
}
