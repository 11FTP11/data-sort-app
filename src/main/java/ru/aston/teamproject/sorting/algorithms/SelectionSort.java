package ru.aston.teamproject.sorting.algorithms;

import ru.aston.teamproject.sorting.SortStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public class SelectionSort<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> data, Comparator<T> comparator) {
        int n = data.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(data.get(j), data.get(minIdx)) < 0) {
                    minIdx = j;
                }
            }
            T temp = data.get(minIdx);
            data.set(minIdx, data.get(i));
            data.set(i, temp);
        }
    }

    @Override
    public void sortEvenOnly(List<T> data, ToIntFunction<T> numberExtractor) {
        int n = data.size();
        for (int i = 0; i < n - 1; i++) {
            T current = data.get(i);
            if (numberExtractor.applyAsInt(current) % 2 != 0) {
                continue;
            }
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                T candidate = data.get(j);
                if (numberExtractor.applyAsInt(candidate) % 2 == 0) {
                    if (numberExtractor.applyAsInt(candidate) < numberExtractor.applyAsInt(data.get(minIdx))) {
                        minIdx = j;
                    }
                }
            }
            if (minIdx != i) {
                T temp = data.get(i);
                data.set(i, data.get(minIdx));
                data.set(minIdx, temp);
            }
        }
    }
}
