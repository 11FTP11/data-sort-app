package ru.aston.teamproject.strategies.filling;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

import java.util.stream.IntStream;
import java.io.PrintStream;

public class RandomFill<E extends Comparable<E>> implements Strategy<E> {
    private final Fillable<E> filler;
    private final int count;
    private final PrintStream out;

    public RandomFill(Fillable<E> filler, int count, PrintStream out) {
        this.filler = filler;
        this.count = count;
        this.out = out;
    }

    @Override
    public void execute(EntityStorage<E> storage) {
        IntStream.range(0, count)
                .mapToObj(i -> filler.createRandom())
                .forEach(storage::add);

        out.println("Сгенерировано элементов: " + count);
    }
}
