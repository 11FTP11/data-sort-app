package ru.aston.teamproject.strategies.search;

import ru.aston.teamproject.collections.EntityStorage;

public class ParallelSearch<E extends Comparable<E>> implements SearchStrategy<E> {

    private final E searchElement;
    private final int threadCount;
    private int coincedenceCount;
    private final Object countLock = new Object(); //для синхронизации, не меняется

    public ParallelSearch(E searchElement, int threadCount){
        if (searchElement == null){
            throw new IllegalArgumentException("Искомый элемент не может быть пустым");
        }
        if (threadCount <= 0){
            throw new IllegalArgumentException("Количество потоков не может быть меньше 0");
        }
        this.searchElement = searchElement;
        int maxThreads = Runtime.getRuntime().availableProcessors(); //получаем количество доступных процессов в системе в принципе
        this.threadCount = Math.min(threadCount, maxThreads); //минимальный из желаемых и доступных потоков
        this.coincedenceCount = 0;
    }


//передаем в основной конструктор искомый элемент и Гет количества потоков
    public ParallelSearch(E searchElement){
        this(searchElement, Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void execute(EntityStorage<E> entityStorage){
        if(entityStorage == null || entityStorage.size() == 0){
            System.out.println("Коллекция пуста. Количество вхождений любого элемента: 0");
            this.coincedenceCount = 0;
            return;
        }
        this.coincedenceCount = 0;
        int size = entityStorage.size();
        if (size < threadCount){ //если элементов меньше, чем потоков- идем последовательно
            searchPosledovatelno(entityStorage, 0, size);
        } else {
            searchParallel(entityStorage, size);
        }
        System.out.println("Результаты поиска:");
        System.out.println("Искомый элемент: "+ searchElement);
        System.out.println("Размер коллекции: " + size);
        System.out.println("Количество потоков: "+ threadCount);
        System.out.println("Количество вхождений элемента в коллекцию: "+ coincedenceCount);
    }

    private void searchPosledovatelno(EntityStorage<E> storage, int startIndex, int endIndex){
        int localCount = 0;
        for(int i = startIndex; i < endIndex; i++){
            if(storage.get(i).compareTo(searchElement) == 0){
                localCount++;
            }
        }
        synchronized (countLock){
            coincedenceCount += localCount;
        }
    }

    private void searchParallel(EntityStorage<E> storage, int size){
        int windowSize = size / threadCount; //определяем окно данных, которое обработает каждый поток
        Thread[] threads = new Thread[threadCount]; //массив ссылок на потоки, ссылки нужны для корректного join

        for (int i = 0; i < threadCount; i++){
            final int startIndex = i * windowSize;
            final int endIndex = (i == threadCount - 1) ? size : startIndex + windowSize;

            threads[i] = new Thread(() -> {
                searchPosledovatelno(storage, startIndex, endIndex);
            });

            threads[i].start();
        }

        for (Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e){
                System.err.println("Поток был прерван: " + e.getMessage());
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    @Override
    public int getCoincndenceCount() {
        synchronized (countLock) { //в один блок может войти только 1 поток благодаря countlock
            return coincedenceCount;
        }
    }

    @Override
    public E getSearchElement() {
        return searchElement;
    }

    public int getThreadCount(){
        return threadCount;
    }
}