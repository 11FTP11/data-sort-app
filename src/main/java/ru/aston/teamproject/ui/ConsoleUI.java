package ru.aston.teamproject.ui;

import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public void printHeader() {
        System.out.println(CYAN + "=".repeat(80) + RESET);
        System.out.println(BOLD + "           СИСТЕМА УПРАВЛЕНИЯ ПОЛЬЗОВАТЕЛЯМИ " + RESET);
        System.out.println(CYAN + "=".repeat(80) + RESET);
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(input);
                if (number >= min && number <= max) return number;
                System.out.println(RED + "Ошибка: введите число от " + min + " до " + max + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Ошибка: '" + input + "' — это не число." + RESET);
            }
        }
    }

    public void printSuccess(String msg) {
        System.out.println(GREEN + "[УСПЕХ] " + msg + RESET);
    }

    public void printError(String msg) {
        System.out.println(RED + "[ОШИБКА] " + msg + RESET);
    }

    public void waitForEnter() {
        System.out.print(CYAN + "\nНажмите Enter для продолжения..." + RESET);
        scanner.nextLine();
    }
}