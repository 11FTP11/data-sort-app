package ru.aston.teamproject.strategies.filling;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

import java.util.stream.IntStream;

public class RandomFill<E extends Comparable<E>> implements Strategy<E> {
    private final Fillable<E> filler;
    private final int count;

    public RandomFill(Fillable<E> filler, int count) {
        this.filler = filler;
        this.count = count;
    }

    @Override
    public void execute(EntityStorage<E> storage) {
        IntStream.range(0, count)
                .mapToObj(i -> filler.createRandom())
                .forEach(storage::add);

        System.out.println("Сгенерировано элементов: " + count);
    }
}