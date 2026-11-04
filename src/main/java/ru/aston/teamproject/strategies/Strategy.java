package ru.aston.teamproject.strategies;

import ru.aston.teamproject.collections.EntityStorage;

public interface Strategy<E extends Comparable<E>> {
    void execute(EntityStorage<E> entityStorage);
}
