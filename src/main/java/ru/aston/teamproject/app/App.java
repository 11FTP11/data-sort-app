package ru.aston.teamproject.app;

import ru.aston.teamproject.storagehandlers.Description;

import ru.aston.teamproject.ui.IoStreams;
import ru.aston.teamproject.ui.ConsoleUI;

import java.util.Objects;

import java.io.PrintStream;

public class App {
    private final ConsoleUI ui;
    private final AppDispatcher dispatcher;
    private String[] menuItems;
    private final IoStreams io;

    public App(IoStreams io, String headerInfo, AppDispatcher dispatcher) {
        this.io = Objects.requireNonNull(io, "io cannot be null");
        headerInfo = Objects.requireNonNull(headerInfo, "headerInfo cannot be null");
        this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher cannot be null");
        
        this.menuItems = null;
        this.ui = new ConsoleUI(io, headerInfo);
    }

    public void linkAll(Description... descriptions) {
        this.menuItems = new String[descriptions.length];

        for (int i = 0; i < descriptions.length; i++) {
            this.menuItems[i] = descriptions[i].text();
            dispatcher.linkRoute(i + 1, descriptions[i].handler());
        }
    }

    public void start() {
        ui.printHeader();
        boolean isRunning = true;
        PrintStream out = this.io.out();
        while (isRunning) {
            out.println("\n" + "\u001B[1m[ГЛАВНОЕ МЕНЮ]\u001B[0m");

            for (int i = 0; i < menuItems.length; i++) {
                out.println((i + 1) + ". " + menuItems[i]);
            }
            out.println("0. Выход");
            out.println("\u001B[36m" + "-".repeat(80) + "\u001B[0m");

            int choice = ui.readInt("Выберите действие: ", 0, menuItems.length);

            if (choice == 0) {
                isRunning = false;
                out.println("Завершение работы программы.");
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
