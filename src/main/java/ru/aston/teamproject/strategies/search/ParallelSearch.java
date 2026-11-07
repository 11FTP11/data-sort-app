package ru.aston.teamproject.strategies.search;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;


public class ParallelSearch<E extends Comparable<E>> implements Strategy<E> {

    private final E searchElement;
    private final int threadCount;
    private int coincidenceCount;
    private final Object countLock = new Object();

    public ParallelSearch(E searchElement, int threadCount) {
        if (searchElement == null) {
            throw new IllegalArgumentException("Искомый элемент не может быть null");
        }
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Количество потоков должно быть больше 0");
        }
        this.searchElement = searchElement;
        this.threadCount = Math.min(threadCount, Runtime.getRuntime().availableProcessors());
        this.coincidenceCount = 0;
    }

    public ParallelSearch(E searchElement) {
        this(searchElement, Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void execute(EntityStorage<E> entityStorage) {
        if (entityStorage == null || entityStorage.size() == 0) {
            System.out.printf("Коллекция пуста. Количество вхождений элемента %s: 0%n", searchElement);
            this.coincidenceCount = 0;
            return;
        }

        this.coincidenceCount = 0;
        int size = entityStorage.size();

        if (size < threadCount) {
            searchSequentially(entityStorage, 0, size);
        } else {
            searchParallel(entityStorage, size);
        }


        System.out.printf("""
                Результаты поиска:
                Искомый элемент: %s
                Количество вхождений: %d
                """, searchElement, coincidenceCount);
    }

    private void searchSequentially(EntityStorage<E> storage, int startIndex, int endIndex) {
        int localCount = 0;
        for (int i = startIndex; i < endIndex; i++) {
            if (storage.get(i).equals(searchElement)) {
                localCount++;
            }
        }
        synchronized (countLock) {
            coincidenceCount += localCount;
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
                System.err.println("Поток был прерван: " + e.getMessage());
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public int getCoincidenceCount() {
        synchronized (countLock) {
            return coincidenceCount;
        }
    }

    public E getSearchElement(){
        return searchElement;
    }

    public int getThreadCount(){
        return threadCount;
    }
}