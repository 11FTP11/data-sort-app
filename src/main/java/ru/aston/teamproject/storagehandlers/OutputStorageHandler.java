package ru.aston.teamproject.storagehandlers;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.strategies.recording.WriteJsonFile;
import ru.aston.teamproject.strategies.recording.WriteOutput;
import ru.aston.teamproject.ui.IoStreams;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintStream;
import java.util.Scanner;

public class OutputStorageHandler<E extends Comparable<E>> extends StorageHandler<E> {

    public OutputStorageHandler(EntityStorage<E> storage, IoStreams io) {
        super(storage, io);
    }

    public void handle() {
        PrintStream out = this.io.out();
        Scanner in = this.io.in();
        EntityContext<E> context = new EntityContext<>();

        while (true) {
            out.print("""
                    <===ВЫВОД===>
                    Куда вывести данные?
                    1. Вывести данные на консоль.
                    2. Вывести данные в файл (создать JSON).
                    3. Выйти из меню вывода.
                    Выберите опцию (1-3):\s""");

            int choice = readInt(in, out);

            switch (choice) {
                case 1 -> context.setStrategy(new WriteOutput<>(out));
                case 2 -> {
                    out.print("Введите имя JSON-файла: ");
                    String fileName = in.nextLine().trim();
                    context.setStrategy(new WriteJsonFile<>(new ObjectMapper(), fileName, this.io));
                }
                case 3 -> {
                    break;
                }
                default -> {
                    continue;
                }
            }

            context.handle(storage);
            break;
        }
    }

    private static int readInt(Scanner in, PrintStream out) {
        while (true) {
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                out.print("Введите число: ");
            }
        }
    }
}
