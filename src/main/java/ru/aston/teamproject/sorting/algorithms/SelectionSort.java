package ru.aston.teamproject.sorting.algorithms;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

import java.util.Comparator;
import java.util.function.ToIntFunction;

public class SelectionSort<E extends Comparable<E>> implements Strategy<E> {

    private final Comparator<E> comparator;

    public SelectionSort() {
        this(Comparator.naturalOrder());
    }

    public SelectionSort(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void execute(EntityStorage<E> data) {
        int n = data.size();
        for (int i = 0; i < n - 1; i++) {
            int bestIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(data.get(j), data.get(bestIdx)) < 0) {
                    bestIdx = j;
                }
            }

            if (bestIdx != i) {
                E temp = data.get(i);
                data.set(i, data.get(bestIdx));
                data.set(bestIdx, temp);
            }
        }
    }

    public void sortEvenOnly(EntityStorage<E> data, Comparator<Integer> intComparator, ToIntFunction<E> numberExtractor) {
        int n = data.size();
        for (int i = 0; i < n - 1; i++) {
            E current = data.get(i);
            int currentNum = numberExtractor.applyAsInt(current);

            if ((currentNum & 1) == 1) {
                continue;
            }

            int bestIdx = i;
            for (int j = i + 1; j < n; j++) {
                E candidate = data.get(j);
                int candidateNum = numberExtractor.applyAsInt(candidate);

                if ((candidateNum & 1) == 0) {

                    if (intComparator.compare(candidateNum, numberExtractor.applyAsInt(data.get(bestIdx))) < 0) {
                        bestIdx = j;
                    }
                }
            }

            if (bestIdx != i) {
                E temp = data.get(i);
                data.set(i, data.get(bestIdx));
                data.set(bestIdx, temp);
            }
        }
    }
}