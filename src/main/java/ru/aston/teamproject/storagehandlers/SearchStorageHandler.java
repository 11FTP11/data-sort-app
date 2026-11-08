package ru.aston.teamproject.storagehandlers;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.Context;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.strategies.search.ParallelSearch;

import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.BiFunction;

import ru.aston.teamproject.ui.IoStreams;

import java.io.PrintStream;
import java.util.Scanner;

public class SearchStorageHandler<E extends Comparable<E>> extends StorageHandler<E> {

    private final int threadCount;
    private final Function<IoStreams, Predicate<E>> predFabric;
    private final BiFunction<EntityStorage<E>, IoStreams, OutputStorageHandler<E>> outHandlerWrapper;

    public SearchStorageHandler(EntityStorage<E> storage, IoStreams io,
            int threadCount, Function<IoStreams, Predicate<E>> predFabric,
            BiFunction<EntityStorage<E>, IoStreams, OutputStorageHandler<E>> outHandlerWrapper) {
        super(storage, io);
        this.threadCount = threadCount;
        this.predFabric = predFabric;
        this.outHandlerWrapper = outHandlerWrapper;
    }

    @Override
    public void handle() {
        PrintStream out = this.io.out();
        Scanner in = this.io.in();
        Context<E> context = new EntityContext<>();

        while (true) {
            out.print("""
                    <===ПОИСК===>
                    Какой результат вам нужен?
                    1. Посчитать количество искомых элементов.
                    2. Вывести искомые элементы (в консоль или в файл).
                    3. Выйти из меню поиска.
                    Выберите опцию (1-3):\s""");

            int choice = readInt(out, in);
            switch (choice) {
                case 1, 2 -> {
                    out.println("Введите информацию об искомой сущности (пустые поля не будут учитываться при поиске)");

                    context.setStrategy(new ParallelSearch<>(
                            predFabric.apply(this.io),
                            (choice == 2) ? this.outHandlerWrapper : null,
                            threadCount, this.io));

                    context.handle(storage);
                    return;
                }
                case 3 -> {
                    return;
                }
            }
        }
    }

    private int readInt(PrintStream out, Scanner in) {
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
