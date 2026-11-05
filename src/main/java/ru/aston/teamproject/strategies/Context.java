package ru.aston.teamproject.strategies;

import ru.aston.teamproject.collections.EntityStorage;

public interface Context<E extends Comparable<E>> {
    void setStrategy(Strategy<E> strategy);

    void handle(EntityStorage<E> storage);
}

