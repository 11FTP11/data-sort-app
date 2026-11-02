package ru.aston.teamproject.sorting.algorithms;

import ru.aston.teamproject.sorting.SortStrategy;

import java.util.Comparator;
import java.util.List;

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
}
