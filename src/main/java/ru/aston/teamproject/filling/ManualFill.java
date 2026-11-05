package ru.aston.teamproject.filling;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.entities.User;
import ru.aston.teamproject.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ManualFill implements Strategy<User> {
    private final int count;
    private final UserValidator validator = new UserValidator();

    public ManualFill(int count) {
        this.count = count;
    }

    @Override
    public void execute(EntityStorage<User> entityStorage) {
        Scanner scanner = new Scanner(System.in);
        List<User> inputUsers = new ArrayList<>();

        IntStream.range(0, count).forEach(i -> {
            boolean isAdded = false;
            while (!isAdded) {
                System.out.println("\n--- Ввод пользователя " + (i + 1) + " из " + count + " ---");
                try {
                    System.out.print("Имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Пароль (мин. 5 символов): ");
                    String pass = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    if (validator.isValid(name, pass, email)) {
                        inputUsers.add(User.builder()
                                .setNickname(name)
                                .setPassword(pass)
                                .setEmail(email)
                                .build());
                        isAdded = true;
                        System.out.println("Добавлен.");
                    } else {
                        System.out.println("Ошибка: проверьте длину пароля и формат email.");
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("\nВвод прерван.");
                    return;
                }
            }
        });

        inputUsers.stream().forEach(entityStorage::add);
    }
}