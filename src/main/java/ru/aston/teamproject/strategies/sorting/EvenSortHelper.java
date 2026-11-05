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
        for (int i = 0; i < data.size(); i++) {
            E element = data.get(i);
            if ((numberExtractor.applyAsInt(element) & 1) == 0) {
                evenStorage.add(element);
            }
        }

        sorter.execute(evenStorage);

        int evenIndex = 0;
        for (int i = 0; i < data.size(); i++) {
            E element = data.get(i);
            if ((numberExtractor.applyAsInt(element) & 1) == 0) {
                data.set(i, evenStorage.get(evenIndex));
                evenIndex++;
            }
        }
    }
}