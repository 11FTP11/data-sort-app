package ru.aston.teamproject.strategies.filling;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.ui.IoStreams;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ManualFill<E extends Comparable<E>> implements Strategy<E> {
    private final Fillable<E> filler;
    private final int count;
    private final IoStreams io;

    public ManualFill(Fillable<E> filler, int count, IoStreams io) {
        this.filler = filler;
        this.count = count;
        this.io = io;
    }

    @Override
    public void execute(EntityStorage<E> storage) {
        EntityStorage<E> tempStorage = new EntityStorage<>();
        PrintStream out = this.io.out();
        PrintStream err = this.io.err();
        Scanner in = this.io.in();
        try {
            IntStream.range(0, count)
                    .forEach(i -> {
                        out.println("Ввод элемента #" + (i + 1));
                        tempStorage.add(filler.createManual(in, out));
                    });

            tempStorage.stream().forEach(storage::add);

        } catch (Exception e) {
            err.println("\nВвод прерван или произошла ошибка: " + e.getMessage());
        }
    }
}
