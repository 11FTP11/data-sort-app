package ru.aston.teamproject.strategies.sorting;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

import java.util.Comparator;
import java.util.function.ToIntFunction;

public class EvenSortHelper {

    public static <E extends Comparable<E>> void sortEvenOnly(
            EntityStorage<E> data,
            Strategy<E> sorter,
            Comparator<Integer> intComparator,
            ToIntFunction<E> numberExtractor) {

        EntityStorage<E> evenStorage = new EntityStorage<>();
        data.stream()
                .filter(e -> (numberExtractor.applyAsInt(e) & 1) == 0)
                .forEach(evenStorage::add);

        sorter.execute(evenStorage);

        int[] evenIndex = {0};
        java.util.stream.IntStream.range(0, data.size())
                .filter(i -> (numberExtractor.applyAsInt(data.get(i)) & 1) == 0)
                .forEach(i -> data.set(i, evenStorage.get(evenIndex[0]++)));
    }
}