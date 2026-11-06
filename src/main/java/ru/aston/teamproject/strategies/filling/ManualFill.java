package ru.aston.teamproject.strategies.filling;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ManualFill<E extends Comparable<E>> implements Strategy<E> {
    private final Fillable<E> filler;
    private final int count;
    private final Scanner in;
    private final PrintStream out;

    public ManualFill(Fillable<E> filler, int count, Scanner in, PrintStream out) {
        this.filler = filler;
        this.count = count;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute(EntityStorage<E> storage) {
        EntityStorage<E> tempStorage = new EntityStorage<>();
        try {
            IntStream.range(0, count)
                    .forEach(i -> {
                        out.println("Ввод элемента #" + (i + 1));
                        tempStorage.add(filler.createManual(in, out));
                    });

            tempStorage.stream().forEach(storage::add);

        } catch (Exception e) {
            out.println("\nВвод прерван или произошла ошибка: " + e.getMessage());
        }
    }
}