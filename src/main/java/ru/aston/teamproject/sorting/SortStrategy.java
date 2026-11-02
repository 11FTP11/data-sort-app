package ru.aston.teamproject.sorting;

import java.util.Comparator;
import java.util.List;

public interface SortStrategy<T> {

    @SuppressWarnings("unchecked")
    default void sort(List<T> data) {
        this.sort(data, (Comparator<T>) Comparator.naturalOrder());
    }

    void sort(List<T> data, Comparator<T> comparator);
}
