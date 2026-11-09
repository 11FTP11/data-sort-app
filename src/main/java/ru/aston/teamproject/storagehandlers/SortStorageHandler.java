package ru.aston.teamproject.storagehandlers;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.strategies.sorting.SortField;
import ru.aston.teamproject.strategies.sorting.SortType;
import ru.aston.teamproject.strategies.sorting.algorithms.BubbleSort;
import ru.aston.teamproject.strategies.sorting.algorithms.QuickSort;
import ru.aston.teamproject.strategies.sorting.algorithms.SelectionSort;
import ru.aston.teamproject.ui.IoStreams;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.ToIntFunction;

public class SortStorageHandler<E extends Comparable<E>> extends StorageHandler<E> {

    private final List<SortField<E>> availableFields;

    public SortStorageHandler(
            EntityStorage<E> storage,
            IoStreams io,
            List<SortField<E>> availableFields) {
        super(storage, io);
        this.availableFields = availableFields;
    }

    @Override
    public void handle() {
        PrintStream out = this.io.out();
        Scanner in = this.io.in();

        while (true) {
            SortType selectedSort = chooseSortType(out, in);
            if (selectedSort == null) {
                return;
            }

            SortField<E> selectedField = chooseSortField(out, in);
            if (selectedField == null) {
                continue;
            }

            boolean isEvenOnly = false;
            if (selectedField.isNumeric()) {
                out.print("""
                        \s
                         Применить дополнительную сортировку?
                         1. Нет (сортировать всё)
                         2. Да (сортировать только четные значения)
                         3. Назад
                         Выберите опцию (1-3):\s""");
                int evenOnlyChoice = readInt(out, in);

                if (evenOnlyChoice == 3) {
                    continue;
                }
                isEvenOnly = (evenOnlyChoice == 2);
            }

            if (isEvenOnly) {
                performEvenOnlySort(out, selectedSort, selectedField);
            } else {
                performStandardSort(out, selectedSort, selectedField);
            }

            return;
        }
    }

    private SortType chooseSortType(PrintStream out, Scanner in) {
        SortType[] availableSorts = SortType.values();
        out.println("<===СОРТИРОВКА===>");
        out.println("Выберите алгоритм сортировки:");

        int index = 1;
        for (SortType sortType : availableSorts) {
            out.printf("%d. %s%n", index++, sortType.getDescription());
        }
        out.printf("%d. Выход%n", index);
        out.print("Выберите опцию (1-" + index + "): ");

        int sortChoice = readInt(out, in);
        if (sortChoice == index) {
            return null;
        }
        if (sortChoice < 1 || sortChoice > availableSorts.length) {
            out.println("НЕИЗВЕСТНАЯ ОПЦИЯ");
            return null;
        }
        return availableSorts[sortChoice - 1];
    }

    private SortField<E> chooseSortField(PrintStream out, Scanner in) {
        out.println("\nВыберите поле для сортировки:");

        int index = 1;
        for (SortField<E> field : availableFields) {
            out.printf("%d. %s%n", index++, field.name());
        }
        out.printf("%d. Назад%n", index);
        out.print("Выберите опцию (1-" + index + "): ");

        int fieldChoice = readInt(out, in);
        if (fieldChoice == index) {
            return null;
        }
        if (fieldChoice < 1 || fieldChoice >= index) {
            out.println("НЕИЗВЕСТНАЯ ОПЦИЯ");
            return null;
        }
        return availableFields.get(fieldChoice - 1);
    }

    private void performStandardSort(PrintStream out, SortType selectedSort, SortField<E> field) {
        @SuppressWarnings("unchecked")
        Strategy<E> strategy = (Strategy<E>) selectedSort.createStrategy(field.comparator());

        EntityContext<E> context = new EntityContext<>();
        context.setStrategy(strategy);
        context.handle(this.storage);

        StringBuilder message = new StringBuilder();
        message.append("\nКоллекция отсортирована.\n")
                .append("Алгоритм: ").append(selectedSort.getDescription()).append("\n")
                .append("Поле: ").append(field.name());

        out.print(message);
    }

    private void performEvenOnlySort(PrintStream out, SortType selectedSort, SortField<E> field) {
        Comparator<Integer> naturalIntComparator = Comparator.naturalOrder();
        ToIntFunction<E> numberExtractor = field.numberExtractor();

        switch (selectedSort) {
            case BUBBLE -> new BubbleSort<E>().sortEvenOnly(this.storage, naturalIntComparator, numberExtractor);
            case SELECTION -> new SelectionSort<E>().sortEvenOnly(this.storage, naturalIntComparator, numberExtractor);
            case QUICK -> new QuickSort<E>().sortEvenOnly(this.storage, naturalIntComparator, numberExtractor);
        }

        StringBuilder message = new StringBuilder();
        message.append("\nВ коллекции были отсортировны только четные значения.\n")
                .append("Алгоритм: ").append(selectedSort.getDescription()).append("\n")
                .append("Поле: ").append(field.name());

        out.print(message);
    }
}