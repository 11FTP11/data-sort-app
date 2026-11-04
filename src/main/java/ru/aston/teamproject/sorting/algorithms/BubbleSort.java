package ru.aston.teamproject.sorting.algorithms;

import ru.aston.teamproject.sorting.SortStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public class BubbleSort<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> data, Comparator<T> comparator) {
        int n = data.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparator.compare(data.get(j), data.get(j + 1)) > 0) {
                    T temp = data.get(j);
                    data.set(j, data.get(j + 1));
                    data.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    @Override
    public void sortEvenOnly(List<T> data, ToIntFunction<T> numberExtractor) {
        int n = data.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                T current = data.get(j);
                T next = data.get(j + 1);

                int currentNumber = numberExtractor.applyAsInt(current);
                int nextNumber = numberExtractor.applyAsInt(next);

                if (currentNumber % 2 == 0 && nextNumber % 2 == 0) {
                    if (currentNumber > nextNumber) {
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
