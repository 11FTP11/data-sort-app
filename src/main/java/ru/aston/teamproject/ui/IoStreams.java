package ru.aston.teamproject.ui;

import java.util.Scanner;
import java.io.PrintStream;

public record IoStreams(
        Scanner in,
        PrintStream out,
        PrintStream err
) {
 
    public IoStreams() {
        this(new Scanner(System.in), System.out, System.err);
    }
}
