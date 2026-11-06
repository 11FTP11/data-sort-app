package ru.aston.teamproject.ui;

import ru.aston.teamproject.AppDispatcher;

public class Menu {
    private final ConsoleUI ui = new ConsoleUI();
    private final AppDispatcher dispatcher;

    public Menu(AppDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void start() {
        ui.printHeader();
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n" + "\u001B[1m[ГЛАВНОЕ МЕНЮ]\u001B[0m");
            System.out.println("1. Ввод данных");
            System.out.println("2. Вывод данных");
            System.out.println("3. Выбор метода сортировки");
            System.out.println("4. Поиск");
            System.out.println("0. Выход");
            System.out.println("\u001B[36m" + "-".repeat(80) + "\u001B[0m");

            int choice = ui.readInt("Выберите действие: ", 0, 4);

            if (choice == 0) {
                isRunning = false;
                System.out.println("Завершение работы программы.");
            } else {
                int result = dispatcher.resolve(choice);
                if (result == -1) {
                    ui.printError("Эта команда еще не реализована командой.");
                }
                ui.waitForEnter();
            }
        }
    }
}