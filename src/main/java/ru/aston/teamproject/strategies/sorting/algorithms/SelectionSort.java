package ru.aston.teamproject.strategies.sorting.algorithms;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.strategies.sorting.EvenSortHelper;

import java.util.Comparator;
import java.util.function.ToIntFunction;

public class SelectionSort<E extends Comparable<E>> implements Strategy<E> {

    private final Comparator<E> comparator;

    public SelectionSort() {
        this(Comparator.naturalOrder());
    }

    public SelectionSort(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void execute(EntityStorage<E> data) {
        int n = data.size();
        for (int i = 0; i < n - 1; i++) {
            int bestIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(data.get(j), data.get(bestIdx)) < 0) {
                    bestIdx = j;
                }
            }

            if (bestIdx != i) {
                E temp = data.get(i);
                data.set(i, data.get(bestIdx));
                data.set(bestIdx, temp);
            }
        }
    }

    public void sortEvenOnly(EntityStorage<E> data, Comparator<Integer> intComparator, ToIntFunction<E> numberExtractor) {
        Comparator<E> evenComparator = (e1, e2) -> intComparator.compare(numberExtractor.applyAsInt(e1), numberExtractor.applyAsInt(e2));
        SelectionSort<E> evenSorter = new SelectionSort<>(evenComparator);
        EvenSortHelper.sortEvenOnly(data, evenSorter, intComparator, numberExtractor);
    }
}