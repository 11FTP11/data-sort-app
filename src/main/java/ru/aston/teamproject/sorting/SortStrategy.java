package ru.aston.teamproject.sorting;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public interface SortStrategy<T> {

    @SuppressWarnings("unchecked")
    default void sort(List<T> data) {
        this.sort(data, (Comparator<T>) Comparator.naturalOrder());
    }

    void sort(List<T> data, Comparator<T> comparator);
    void sortEvenOnly(List<T> data, ToIntFunction<T> numberExtractor);
}
