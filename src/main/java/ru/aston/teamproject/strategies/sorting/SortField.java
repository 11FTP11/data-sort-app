package ru.aston.teamproject.strategies.sorting;

import java.util.Comparator;
import java.util.function.ToIntFunction;

public record SortField<E>(
        String name,
        Comparator<E> comparator,
        boolean isNumeric,
        ToIntFunction<E> numberExtractor
) {
}