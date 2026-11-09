package ru.aston.teamproject.strategies.filling;

import java.io.PrintStream;
import java.util.Scanner;

public interface Fillable<E> {
    E createRandom();

    E createManual(Scanner in, PrintStream out);
}