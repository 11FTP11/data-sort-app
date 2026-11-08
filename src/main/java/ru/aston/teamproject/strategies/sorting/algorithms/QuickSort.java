package ru.aston.teamproject.strategies.sorting.algorithms;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.strategies.sorting.EvenSortHelper;

import java.util.Comparator;
import java.util.function.ToIntFunction;

import static java.util.Collections.swap;

public class QuickSort<E extends Comparable<E>> implements Strategy<E> {

    private final Comparator<E> comparator;

    public QuickSort() {
        this(Comparator.naturalOrder());
    }

    public QuickSort(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void execute(EntityStorage<E> data) {
        if (data.size() <= 1) {
            return;
        }
        quickSort(data, 0, data.size() - 1);
    }

    private void quickSort(EntityStorage<E> data, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(data, low, high);
            quickSort(data, low, pivotIndex - 1);
            quickSort(data, pivotIndex + 1, high);
        }
    }

    private int partition(EntityStorage<E> data, int low, int high) {
        E pivot = data.get(high);
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            E current = data.get(j);

            if (comparator.compare(current, pivot) <= 0) {
                i++;
                swap(data, i, j);
            }
        }

        swap(data, i + 1, high);
        return i + 1;
    }

    public void sortEvenOnly(EntityStorage<E> data, Comparator<Integer> intComparator, ToIntFunction<E> numberExtractor) {
        Comparator<E> evenComparator = (e1, e2) -> intComparator.compare(numberExtractor.applyAsInt(e1), numberExtractor.applyAsInt(e2));
        QuickSort<E> evenSorter = new QuickSort<>(evenComparator);
        EvenSortHelper.sortEvenOnly(data, evenSorter, intComparator, numberExtractor);
    }
}