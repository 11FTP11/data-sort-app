package ru.aston.teamproject.storagehandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.strategies.filling.Fillable;
import ru.aston.teamproject.strategies.filling.FileFill;
import ru.aston.teamproject.strategies.filling.ManualFill;
import ru.aston.teamproject.strategies.filling.RandomFill;
import ru.aston.teamproject.ui.IoStreams;

import java.io.PrintStream;
import java.util.Scanner;

public class FillStorageHandler<E extends Comparable<E>> extends StorageHandler<E> {
    private final Fillable<E> filler;
    private final Class<E> clazz;

    public FillStorageHandler(
            EntityStorage<E> storage,
            IoStreams io,
            Fillable<E> filler,
            Class<E> clazz) {
        super(storage, io);
        this.filler = filler;
        this.clazz = clazz;
    }

    @Override
    public void handle() {
        PrintStream out = this.io.out();
        Scanner in = this.io.in();
        out.print("""
                <===ЗАПОЛНЕНИЕ===>
                1. Случайными значениями.
                2. Из файла (JSON).
                3. Вручную.
                4. Выйти из меню заполнения.
                Выберите опцию (1-4):\s""");

        int choice = readInt(out, in);

        EntityContext<E> context = new EntityContext<>();

        switch (choice) {
            case 1 -> {
                out.print("Сколько элементов сгенерировать? ");
                int count = readInt(out, in);
                context.setStrategy(new RandomFill<>(filler, count, out));
            }
            case 2 -> {
                out.print("Введите имя JSON-файла (в папке resources) или полный путь к файлу на диске: ");
                String fileName = in.nextLine().trim();
                context.setStrategy(new FileFill<>(new ObjectMapper(), fileName, clazz, this.io));
            }
            case 3 -> {
                out.print("Сколько элементов ввести вручную? ");
                int count = readInt(out, in);
                context.setStrategy(new ManualFill<>(filler, count, this.io));
            }
            default -> {
                return;
            }
        }

        context.handle(this.storage);
    }
}
