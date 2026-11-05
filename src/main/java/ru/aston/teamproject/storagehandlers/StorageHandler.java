package ru.aston.teamproject.storagehandlers;

import ru.aston.teamproject.collections.EntityStorage;

import java.io.PrintStream;
import java.util.Scanner;

public abstract class StorageHandler<E extends Comparable<E>> {
    protected final EntityStorage<E> storage;
    protected final Scanner in;
    protected final PrintStream out;

    public StorageHandler(EntityStorage<E> storage, Scanner scanner, PrintStream out) {
        this.storage = storage;
        this.in = scanner;
        this.out = out;
    }

    public abstract void handle();
}
