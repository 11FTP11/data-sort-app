package ru.aston.teamproject.storagehandlers;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.contexts.EntityContext;
import ru.aston.teamproject.strategies.Strategy;

import ru.aston.teamproject.strategies.sorting.SortType;
import ru.aston.teamproject.strategies.sorting.algorithms.BubbleSort;
import ru.aston.teamproject.strategies.sorting.algorithms.QuickSort;
import ru.aston.teamproject.strategies.sorting.algorithms.SelectionSort;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.ToIntFunction;

public class SortStorageHandler<E extends Comparable<E>> extends StorageHandler<E> {

    public record SortField<E>(
            String name,
            Comparator<E> comparator,
            boolean isNumeric,
            ToIntFunction<E> numberExtractor
    ) {}

    private final List<SortField<E>> availableFields;

    public SortStorageHandler(
            EntityStorage<E> storage,
            Scanner scanner,
            PrintStream out,
            List<SortField<E>> availableFields) {
        super(storage, scanner, out);
        this.availableFields = availableFields;
    }

    @Override
    public void handle() {
        SortType[] availableSorts = SortType.values();
        this.out.println("<===СОРТИРОВКА===>");
        this.out.println("Выберите алгоритм сортировки:");

        int index = 1;
        for (SortType sortType : availableSorts) {
            this.out.printf("%d. %s%n", index++, sortType.getDescription());
        }
        this.out.print("Выберите опцию (1-" + availableSorts.length + "): ");

        int sortChoice = readInt();
        if (sortChoice < 1 || sortChoice > availableSorts.length) {
            this.out.println("НЕИЗВЕСТНАЯ ОПЦИЯ");
            return;
        }
        SortType selectedSort = availableSorts[sortChoice - 1];

        SortField<E> selectedField = askForField();
        if (selectedField == null) {
            this.out.println("Сортировка отменена.");
            return;
        }

        boolean isEvenOnly = false;
        if (selectedField.isNumeric()) {
            this.out.print("""
                    
                    Применить дополнительную сортировку?
                    1. Нет (сортировать всё)
                    2. Да (сортировать только четные значения)
                    Выберите опцию (1-2): """);
            int evenOnlyChoice = readInt();
            isEvenOnly = (evenOnlyChoice == 2);
        }

        if (isEvenOnly) {
            performEvenOnlySort(selectedSort, selectedField.numberExtractor());
        } else {
            performStandardSort(selectedSort, selectedField.comparator());
        }
    }

    private void performStandardSort(SortType selectedSort, Comparator<E> comparator) {
        @SuppressWarnings("unchecked")
        Strategy<E> strategy = (Strategy<E>) selectedSort.createStrategy(comparator);

        EntityContext<E> context = new EntityContext<>();
        context.setStrategy(strategy);
        context.handle(this.storage);
    }

    private void performEvenOnlySort(SortType selectedSort, ToIntFunction<E> numberExtractor) {
        Comparator<Integer> naturalIntComparator = Comparator.naturalOrder();

        switch (selectedSort) {
            case BUBBLE -> new BubbleSort<E>().sortEvenOnly(this.storage, naturalIntComparator, numberExtractor);
            case SELECTION -> new SelectionSort<E>().sortEvenOnly(this.storage, naturalIntComparator, numberExtractor);
            case QUICK -> new QuickSort<E>().sortEvenOnly(this.storage, naturalIntComparator, numberExtractor);
        }
    }

    private SortField<E> askForField() {
        this.out.println("\nПо какому полю сортировать?");

        int index = 1;
        for (SortField<E> field : availableFields) {
            this.out.printf("%d. %s%n", index++, field.name());
        }

        this.out.printf("%d. Отмена%n", index);
        this.out.print("Выберите опцию (1-" + index + "): ");

        int fieldChoice = readInt();

        if (fieldChoice < 1 || fieldChoice >= index) {
            return null;
        }

        return availableFields.get(fieldChoice - 1);
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(this.in.next());
            } catch (NumberFormatException e) {
                this.out.print("Ошибка: введите целое число: ");
            }
        }
    }
}