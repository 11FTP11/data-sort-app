package ru.aston.teamproject.storagehandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.strategies.filling.Fillable;
import ru.aston.teamproject.strategies.filling.FileFill;
import ru.aston.teamproject.strategies.filling.ManualFill;
import ru.aston.teamproject.strategies.filling.RandomFill;

import java.io.PrintStream;
import java.util.Scanner;

public class FillStorageHandler<E extends Comparable<E>> extends StorageHandler<E> {
    private final Fillable<E> filler;
    private final ObjectMapper mapper;
    private final Class<E> clazz;

    public FillStorageHandler(
            EntityStorage<E> storage,
            Scanner scanner,
            PrintStream out,
            Fillable<E> filler,
            ObjectMapper mapper,
            Class<E> clazz) {
        super(storage, scanner, out);
        this.filler = filler;
        this.mapper = mapper;
        this.clazz = clazz;
    }

    @Override
    public void handle() {
        this.out.print("""
                <===ЗАПОЛНЕНИЕ===>
                1. Случайными значениями
                2. Из файла (JSON)
                3. Вручную
                4. Назад
                Выберите опцию (1-4):\s""");

        int choice = readInt();

        EntityContext<E> context = new EntityContext<>();

        switch (choice) {
            case 1 -> {
                this.out.print("Сколько элементов сгенерировать? ");
                int count = readInt();
                context.setStrategy(new RandomFill<>(filler, count));
            }
            case 2 -> {
                this.out.print("Введите имя JSON-файла: ");
                String fileName = this.in.next();
                context.setStrategy(new FileFill<>(mapper, fileName, clazz));
            }
            case 3 -> {
                this.out.print("Сколько элементов ввести вручную? ");
                int count = readInt();
                context.setStrategy(new ManualFill<>(filler, count, this.in, this.out));
            }
            default -> {
                this.out.println("Возврат в главное меню.");
                return;
            }
        }

        context.handle(this.storage);
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