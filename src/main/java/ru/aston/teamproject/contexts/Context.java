package ru.aston.teamproject.contexts;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

public interface Context<E extends Comparable<E>> {
    void setStrategy(Strategy<E> strategy);
    void handle(EntityStorage<E> storage);
}

