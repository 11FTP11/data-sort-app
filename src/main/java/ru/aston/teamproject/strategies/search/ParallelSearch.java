package ru.aston.teamproject.strategies.search;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.ui.IoStreams;
import ru.aston.teamproject.storagehandlers.OutputStorageHandler;

import java.util.function.Predicate;
import java.util.function.BiFunction;

import java.util.Objects;

public class ParallelSearch<E extends Comparable<E>> implements Strategy<E> {

    private final int threadCount;
    private int coincidenceCount;
    private EntityStorage<E> coincidenceArr;
    private final Predicate<E> pred;
    private final BiFunction<EntityStorage<E>, IoStreams, OutputStorageHandler<E>> outHandlerWrapper;
    private final IoStreams io;
    private final Object countLock = new Object();

    public ParallelSearch(Predicate<E> pred,
            BiFunction<EntityStorage<E>, IoStreams, OutputStorageHandler<E>> outHandlerWrapper, int threadCount,
            IoStreams io) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Количество потоков должно быть больше 0");
        }

        this.pred = Objects.requireNonNull(pred, "Предикат поиска не может быть null");
        this.io = Objects.requireNonNull(io, "IoStreams не может быть null");
        this.outHandlerWrapper = outHandlerWrapper;
        this.threadCount = Math.min(threadCount, Runtime.getRuntime().availableProcessors());
        this.coincidenceCount = 0;

        if (this.outHandlerWrapper != null) {
            this.coincidenceArr = new EntityStorage<>();
        }
    }

    @Override
    public void execute(EntityStorage<E> entityStorage) {
        this.coincidenceCount = 0;
        if (this.outHandlerWrapper != null) {
            this.coincidenceArr.clear();
        }

        if (entityStorage == null || entityStorage.size() == 0) {
            this.io.out().println("Коллекция пуста. Искать нечего!");
            this.coincidenceCount = 0;
            return;
        }

        int size = entityStorage.size();

        if (size < threadCount) {
            searchSequentially(entityStorage, 0, size);
        } else {
            searchParallel(entityStorage, size);
        }

        this.io.out().println("Количество вхождений: " + coincidenceCount);
        if (this.outHandlerWrapper != null) {
            OutputStorageHandler<E> handler = this.outHandlerWrapper.apply(this.coincidenceArr, this.io);
            Objects.requireNonNull(handler, "Обработчик для записи данных не должен быть null");
            handler.handle();
        }
    }

    private void searchSequentially(EntityStorage<E> storage, int startIndex, int endIndex) {
        int localCount = 0;
        EntityStorage<E> localArr = new EntityStorage<>();
        for (int i = startIndex; i < endIndex; i++) {
            E value = storage.get(i);
            if (this.pred.test(value)) {
                localArr.add(value);
                localCount++;
            }
        }
        synchronized (countLock) {
            if (this.outHandlerWrapper != null) {
                this.coincidenceArr.addAll(localArr);
            }

            this.coincidenceCount += localCount;
        }
    }

    private void searchParallel(EntityStorage<E> storage, int size) {
        int chunkSize = size / threadCount;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int startIndex = i * chunkSize;
            final int endIndex = (i == threadCount - 1) ? size : startIndex + chunkSize;

            threads[i] = new Thread(() -> {
                searchSequentially(storage, startIndex, endIndex);
            }, "Search-Thread-" + i);

            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                this.io.err().println("Поток был прерван: " + e.getMessage());
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public int getCoincidenceCount() {
        return coincidenceCount;
    }
}
