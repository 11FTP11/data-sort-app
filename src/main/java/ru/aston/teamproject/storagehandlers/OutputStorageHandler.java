package ru.aston.teamproject.storagehandlers;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.strategies.recording.WriteJsonFile;
import ru.aston.teamproject.strategies.recording.WriteOutput;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintStream;
import java.util.Scanner;

public class OutputStorageHandler<E extends Comparable<E>> extends StorageHandler<E> {

    public OutputStorageHandler(EntityStorage<E> storage, Scanner scanner, PrintStream out) {
        super(storage, scanner, out);
    }

    public void handle() {
        this.out.print("""
                <===ВЫВОД===>
                Куда вывести данные?
                1. Вывести данные на консоль.
                2. Вывести данные в файл (создать JSON)
                Выберите опцию (1-2):\s""");

        EntityContext<E> context = new EntityContext<>();

        switch (this.in.nextInt()) {
            case 1:
                context.setStrategy(new WriteOutput<>(this.out));
                break;
            case 2:
                this.out.print("Введите имя JSON-файла:");
                context.setStrategy(new WriteJsonFile<>(new ObjectMapper(), this.in.next()));
                break;
            default:
                this.out.print("НЕИЗВЕСТНАЯ ОПЦИЯ");
                return;
        }

        context.handle(storage);
    }
}
