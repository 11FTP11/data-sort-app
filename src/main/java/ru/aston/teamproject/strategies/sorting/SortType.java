package ru.aston.teamproject.strategies.sorting;

import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.strategies.sorting.algorithms.BubbleSort;
import ru.aston.teamproject.strategies.sorting.algorithms.QuickSort;
import ru.aston.teamproject.strategies.sorting.algorithms.SelectionSort;

import java.util.Comparator;
import java.util.function.Function;

public enum SortType {
    BUBBLE("Пузырьковая сортировка", BubbleSort::new),
    SELECTION("Сортировка выбором", SelectionSort::new),
    QUICK("Быстрая сортировка", QuickSort::new);

    private final String description;
    private final Function<Comparator, Strategy> factory;

    SortType(String description, Function<Comparator, Strategy> factory) {
        this.description = description;
        this.factory = factory;
    }

    public String getDescription() {
        return description;
    }

    public Strategy<?> createStrategy(Comparator<?> comparator) {
        return factory.apply(comparator);
    }
}