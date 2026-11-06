package ru.aston.teamproject;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.storagehandlers.Description;
import ru.aston.teamproject.storagehandlers.StorageHandler;
import ru.aston.teamproject.ui.Menu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AppDispatcher dispatcher = new AppDispatcher();

        EntityStorage<String> tempStorage = new EntityStorage<>();
        Scanner scanner = new Scanner(System.in);

        StorageHandler<String> stub1 = new StorageHandler<>(tempStorage, scanner, System.out) {
            @Override
            public void handle() {
                System.out.println("[ЗАГЛУШКА] Ввод данных в разработке...");
            }
        };

        StorageHandler<String> stub2 = new StorageHandler<>(tempStorage, scanner, System.out) {
            @Override
            public void handle() {
                System.out.println("[ЗАГЛУШКА] Вывод данных в разработке...");
            }
        };

        StorageHandler<String> stub3 = new StorageHandler<>(tempStorage, scanner, System.out) {
            @Override
            public void handle() {
                System.out.println("[ЗАГЛУШКА] Сортировка в разработке...");
            }
        };

        StorageHandler<String> stub4 = new StorageHandler<>(tempStorage, scanner, System.out) {
            @Override
            public void handle() {
                System.out.println("[ЗАГЛУШКА] Поиск в разработке...");
            }
        };

        Menu menu = new Menu(dispatcher,
                new Description("Ввод данных", stub1),
                new Description("Вывод данных", stub2),
                new Description("Выбор метода сортировки", stub3),
                new Description("Поиск", stub4)
        );

        menu.start();
    }
}