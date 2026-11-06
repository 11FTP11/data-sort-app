package ru.aston.teamproject;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.storagehandlers.StorageHandler;
import ru.aston.teamproject.ui.Menu;

import java.util.Scanner;

public class App {
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

        dispatcher.linkRoute(1, stub1)
                .linkRoute(2, stub2)
                .linkRoute(3, stub3)
                .linkRoute(4, stub4);

        Menu menu = new Menu(dispatcher);
        menu.start();
    }
}