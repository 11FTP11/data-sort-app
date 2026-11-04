package ru.aston.teamproject.sorting.algorithms;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

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
        int n = data.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                E current = data.get(j);
                E next = data.get(j + 1);

                int currentNumber = numberExtractor.applyAsInt(current);
                int nextNumber = numberExtractor.applyAsInt(next);

                if ((currentNumber & 1) == 0 && (nextNumber & 1) == 0) {
                    if (intComparator.compare(currentNumber, nextNumber) > 0) {
                        data.set(j, next);
                        data.set(j + 1, current);
                        swapped = true;
                    }
                }
            }
            if (!swapped) break;
        }
    }
}
