package ru.aston.teamproject;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.storagehandlers.Description;
import ru.aston.teamproject.storagehandlers.StorageHandler;
import ru.aston.teamproject.ui.App;
import ru.aston.teamproject.ui.IoStreams;

public class Main {
    public static void main(String[] args) {

        EntityStorage<String> tempStorage = new EntityStorage<>();
        IoStreams io = new IoStreams();

        StorageHandler<String> stub1 = new StorageHandler<>(tempStorage, io) {
            @Override
            public void handle() {
                System.out.println("[ЗАГЛУШКА] Ввод данных в разработке...");
            }
        };

        StorageHandler<String> stub2 = new StorageHandler<>(tempStorage, io) {
            @Override
            public void handle() {
                System.out.println("[ЗАГЛУШКА] Вывод данных в разработке...");
            }
        };

        StorageHandler<String> stub3 = new StorageHandler<>(tempStorage, io) {
            @Override
            public void handle() {
                System.out.println("[ЗАГЛУШКА] Сортировка в разработке...");
            }
        };

        StorageHandler<String> stub4 = new StorageHandler<>(tempStorage, io) {
            @Override
            public void handle() {
                System.out.println("[ЗАГЛУШКА] Поиск в разработке...");
            }
        };

        App app = new App(new AppDispatcher(),
                new Description("Заполнение хранилища", stub1),
                new Description("Вывод элементов хранилища", stub2),
                new Description("Cортировка хранилища", stub3),
                new Description("Поиск элемента в хранилище", stub4));

        app.start();
    }
}
