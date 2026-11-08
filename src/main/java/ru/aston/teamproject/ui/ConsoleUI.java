package ru.aston.teamproject.ui;

import java.util.Scanner;
import java.io.PrintStream;

public class ConsoleUI {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";
    private final IoStreams io;
    private final String header;

    public void printHeader() {
        PrintStream out = this.io.out();
        out.println(CYAN + "=".repeat(80) + RESET);
        out.println(BOLD + String.format("           %s", this.header) + RESET);
        out.println(CYAN + "=".repeat(80) + RESET);
    }

    public ConsoleUI(IoStreams io, String header) {
        this.io = io;
        this.header = header;
    }

    public int readInt(String prompt, int min, int max) {
        PrintStream out = this.io.out();        
        Scanner in = this.io.in();
        while (true) {
            out.print(prompt);
            String input = in.nextLine().trim();
            try {
                int number = Integer.parseInt(input);
                if (number >= min && number <= max) return number;
                out.println(RED + "Ошибка: введите число от " + min + " до " + max + RESET);
            } catch (NumberFormatException e) {
                out.println(RED + "Ошибка: '" + input + "' — это не число." + RESET);
            }
        }
    }

    public void printError(String msg) {
        this.io.out().println(RED + "[ОШИБКА] " + msg + RESET);
    }

    public void waitForEnter() {
        this.io.out().print(CYAN + "\nНажмите Enter для продолжения..." + RESET);
        this.io.in().nextLine();
    }
}
