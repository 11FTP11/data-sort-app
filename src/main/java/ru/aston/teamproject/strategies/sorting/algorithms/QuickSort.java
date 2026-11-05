package ru.aston.teamproject.strategies.sorting.algorithms;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.strategies.sorting.EvenSortHelper;

import java.util.Comparator;
import java.util.function.ToIntFunction;

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

        Object[] array = data.toArray();

        quickSort(array, 0, array.length - 1);

        for (int i = 0; i < array.length; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) array[i];
            data.set(i, element);
        }
    }

    private void quickSort(Object[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);

            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    @SuppressWarnings("unchecked")
    private int partition(Object[] array, int low, int high) {
        E pivot = (E) array[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            E current = (E) array[j];
            if (comparator.compare(current, pivot) <= 0) {
                i++;
                Object temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        Object temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    public void sortEvenOnly(EntityStorage<E> data, Comparator<Integer> intComparator, ToIntFunction<E> numberExtractor) {
        Comparator<E> evenComparator = (e1, e2) -> intComparator.compare(numberExtractor.applyAsInt(e1), numberExtractor.applyAsInt(e2));
        QuickSort<E> evenSorter = new QuickSort<>(evenComparator);
        EvenSortHelper.sortEvenOnly(data, evenSorter, intComparator, numberExtractor);
    }
}