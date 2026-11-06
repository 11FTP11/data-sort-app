package ru.aston.teamproject.ui;

import ru.aston.teamproject.AppDispatcher;
import ru.aston.teamproject.storagehandlers.Description;

public class App {
    private final ConsoleUI ui = new ConsoleUI();
    private final AppDispatcher dispatcher;
    private final Description[] descriptions;

    public App(AppDispatcher dispatcher, Description... descriptions) {
        this.dispatcher = dispatcher;
        this.descriptions = descriptions;

        for (int i = 0; i < descriptions.length; i++) {
            dispatcher.linkRoute(i + 1, descriptions[i].handler());
        }
    }

    public void start() {
        ui.printHeader();
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n" + "\u001B[1m[ГЛАВНОЕ МЕНЮ]\u001B[0m");

            for (int i = 0; i < descriptions.length; i++) {
                System.out.println((i + 1) + ". " + descriptions[i].text());
            }
            System.out.println("0. Выход");
            System.out.println("\u001B[36m" + "-".repeat(80) + "\u001B[0m");

            int choice = ui.readInt("Выберите действие: ", 0, descriptions.length);

            if (choice == 0) {
                isRunning = false;
                System.out.println("Завершение работы программы.");
            } else {
                int result = dispatcher.resolve(choice);
                if (result == -1) {
                    ui.printError("Неизвестная опция.");
                }
                ui.waitForEnter();
            }
        }
    }
}