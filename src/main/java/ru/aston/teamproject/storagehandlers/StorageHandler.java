package ru.aston.teamproject.storagehandlers;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.ui.IoStreams;

import java.io.PrintStream;
import java.util.Scanner;

public abstract class StorageHandler<E extends Comparable<E>> {
    protected final EntityStorage<E> storage;
    protected final IoStreams io;

    public StorageHandler(EntityStorage<E> storage, IoStreams io) {
        this.storage = storage;
        this.io = io;
    }

    public abstract void handle();

    public int readInt(PrintStream out, Scanner in) {
        while (true) {
            String line = in.hasNextLine() ? in.nextLine().trim() : "";
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                out.print("Ошибка: введите целое число: ");
            }
        }
    }
}
