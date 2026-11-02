package ru.aston.teamproject.sorting;

import java.util.Comparator;
import java.util.List;

public interface SortStrategy<T> {
    void sort(List<T> data);
    void sort(List<T> data, Comparator<T> comparator);
}
