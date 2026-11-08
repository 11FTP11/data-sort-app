package ru.aston.teamproject.strategies.recording;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

import java.io.PrintStream;

public class WriteOutput<E extends Comparable<E>> implements Strategy<E> {
    private final PrintStream out;

    public WriteOutput(PrintStream out) {
        this.out = out;
    }

    @Override
    public void execute(EntityStorage<E> storage) {
        PrintStream out = this.out;
        if (storage == null || storage.size() == 0) {
            out.println("Хранилище пустое");
        } else {
            storage.stream().forEach(entity -> out.println(entity.toString()));
        }
    }
}
