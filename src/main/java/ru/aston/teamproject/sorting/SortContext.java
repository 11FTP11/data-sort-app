package ru.aston.teamproject.sorting;

import java.util.Comparator;
import java.util.List;

public class SortContext<T> {
    private final String ERROR_MESSAGE = "[Exception]: Стратегия сортировки не установлена.";
    private SortStrategy<T> strategy;

    public void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void handle(List<T> data) {
        if (strategy == null) {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
        strategy.sort(data);
    }

    public void handle(List<T> data, Comparator<T> comparator) {
        if (strategy == null) {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
        strategy.sort(data, comparator);
    }
}
