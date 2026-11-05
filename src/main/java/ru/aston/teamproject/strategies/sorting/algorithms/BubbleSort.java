package ru.aston.teamproject.strategies.sorting.algorithms;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.strategies.sorting.EvenSortHelper;

import java.util.Comparator;
import java.util.function.ToIntFunction;

public class BubbleSort<E extends Comparable<E>> implements Strategy<E> {

    private final Comparator<E> comparator;

    public BubbleSort() {
        this(Comparator.naturalOrder());
    }

    public BubbleSort(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void execute(EntityStorage<E> data) {
        int n = data.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparator.compare(data.get(j), data.get(j + 1)) > 0) {
                    E temp = data.get(j);
                    data.set(j, data.get(j + 1));
                    data.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public void sortEvenOnly(EntityStorage<E> data, Comparator<Integer> intComparator, ToIntFunction<E> numberExtractor) {
        Comparator<E> evenComparator = (e1, e2) -> intComparator.compare(numberExtractor.applyAsInt(e1), numberExtractor.applyAsInt(e2));
        BubbleSort<E> evenSorter = new BubbleSort<>(evenComparator);
        EvenSortHelper.sortEvenOnly(data, evenSorter, intComparator, numberExtractor);
    }
}
