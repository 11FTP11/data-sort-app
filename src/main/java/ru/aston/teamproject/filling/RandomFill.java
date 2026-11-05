package ru.aston.teamproject.filling;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.entities.User;
import ru.aston.teamproject.strategies.Strategy;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomFill implements Strategy<User> {
    private final int count;
    private final Random random = new Random();
    private final UserValidator validator = new UserValidator();

    private final String[] names = {"Mark", "Maria", "Ivan", "Olga", "Dmitriy", "Elena", "Denis", "Aleksey", "Arina", "Maxim", "Magomed", "Albert", "Kirill", "Anastasia", "Larisa", "Victoria", "Victor", "Mihail", "Anatoliy", "Oleg", "Karina", "Valentina", "Fatima", "Kerim"};
    private final String[] domains = {"gmail.com", "yandex.ru", "ya.ru", "mail.ru", "list.ru", "bk.ru", "inbox.ru", "xmail.ru"};

    public RandomFill(int count) {
        this.count = count;
    }

    @Override
    public void execute(EntityStorage<User> entityStorage) {
        IntStream.range(0, count)
                .mapToObj(i -> generateRandomUser())
                .filter(user -> user != null)
                .forEach(entityStorage::add);
    }

    private User generateRandomUser() {
        String name = names[random.nextInt(names.length)];
        String pass = "pass" + (random.nextInt(9000) + 1000);
        String email = name.toLowerCase() + random.nextInt(100) + "@" + domains[random.nextInt(domains.length)];

        if (validator.isValid(name, pass, email)) {
            return User.builder()
                    .setNickname(name)
                    .setPassword(pass)
                    .setEmail(email)
                    .build();
        }
        return null;
    }
}