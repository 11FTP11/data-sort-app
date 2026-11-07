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
        SortType[] availableSorts = SortType.values();
        PrintStream out = this.io.out();
        Scanner in = this.io.in();
        out.println("<===СОРТИРОВКА===>");
        out.println("Выберите алгоритм сортировки:");

        int index = 1;
        for (SortType sortType : availableSorts) {
            out.printf("%d. %s%n", index++, sortType.getDescription());
        }
        out.print("Выберите опцию (1-" + availableSorts.length + "): ");

        int sortChoice = readInt(out, in);
        if (sortChoice < 1 || sortChoice > availableSorts.length) {
            out.println("НЕИЗВЕСТНАЯ ОПЦИЯ");
            return;
        }
        SortType selectedSort = availableSorts[sortChoice - 1];

        SortField<E> selectedField = askForField(out, in);
        if (selectedField == null) {
            out.println("Сортировка отменена.");
            return;
        }

        boolean isEvenOnly = false;
        if (selectedField.isNumeric()) {
            out.print("""
                    \s
                     Применить дополнительную сортировку?
                     1. Нет (сортировать всё)
                     2. Да (сортировать только четные значения)
                     Выберите опцию (1-2):\s""");
            int evenOnlyChoice = readInt(out, in);
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

    private SortField<E> askForField(PrintStream out,  Scanner in) {

        out.println("\nПо какому полю сортировать?");

        int index = 1;
        for (SortField<E> field : availableFields) {
            out.printf("%d. %s%n", index++, field.name());
        }

        out.printf("%d. Отмена%n", index);
        out.print("Выберите опцию (1-" + index + "): ");

        int fieldChoice = readInt(out, in);

        if (fieldChoice < 1 || fieldChoice >= index) {
            return null;
        }

        return availableFields.get(fieldChoice - 1);
    }

    private int readInt(PrintStream out, Scanner in) {
        while (true) {
            try {
                return Integer.parseInt(in.next());
            } catch (NumberFormatException e) {
                out.print("Ошибка: введите целое число: ");
            }
        }
    }
}
