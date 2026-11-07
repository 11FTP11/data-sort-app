package ru.aston.teamproject.storagehandlers;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.Context;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.strategies.search.ParallelSearch;

import java.io.PrintStream;
import java.util.Scanner;

public class SearchStorageHandler<E extends Comparable<E>> extends StorageHandler<E> {

    private final E searchElement;
    private final int threadCount;

    public SearchStorageHandler(EntityStorage<E> storage, Scanner in, PrintStream out, E searchElement, int threadCount) {
        super(storage, in, out);
        this.searchElement = searchElement;
        this.threadCount = threadCount;
    }

    @Override
    public void handle() {
        out.println("Начинается поиск элемента: " + searchElement);

        ParallelSearch<E> searchStrategy = new ParallelSearch<>(searchElement, threadCount);

        Context<E> context = new EntityContext<>();

        context.setStrategy(searchStrategy);
        context.handle(storage);

        out.println("Хотите записать результат поиска в файл? (да/нет)");
        String answer = in.next();
        if ("да".equalsIgnoreCase(answer) || "y".equalsIgnoreCase(answer)) {
            out.println("Здесь будет вызван OutputStorageHandler или стратегия записи для сохранения результата: "
                    + searchStrategy.getCoincidenceCount());
        }
    }
}