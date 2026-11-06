package ru.aston.teamproject.filling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.entities.User;
import ru.aston.teamproject.strategies.filling.FileFill;
import ru.aston.teamproject.strategies.filling.ManualFill;
import ru.aston.teamproject.strategies.filling.RandomFill;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FillTest {

    @Test
    @DisplayName("RandomFill: генерирует указанное количество пользователей")
    void when_executeRandomFillWithCount_then_storageSizeEqualsCount() {
        EntityStorage<User> storage = new EntityStorage<>();
        RandomFill<User> randomFill = new RandomFill<>(new User.UserBuilder(), 5);

        randomFill.execute(storage);

        assertEquals(5, storage.size(), "Должно быть сгенерировано 5 пользователей");
    }

    @Test
    @DisplayName("EntityContext: вызывает стратегию и наполняет коллекцию")
    void when_contextHandlesWithStrategy_then_strategyIsInvokedAndStorageFilled() {
        EntityStorage<User> storage = new EntityStorage<>();
        EntityContext<User> context = new EntityContext<>();

        context.setStrategy(new RandomFill<>(new User.UserBuilder(), 3));
        context.handle(storage);

        assertEquals(3, storage.size(), "Контекст должен наполнить коллекцию 3 элементами");
    }

    @Test
    @DisplayName("EntityContext: выбрасывает исключение без стратегии")
    void when_contextHandlesWithoutStrategy_then_throwsNullPointerException() {
        EntityContext<User> context = new EntityContext<>();
        EntityStorage<User> storage = new EntityStorage<>();

        assertThrows(NullPointerException.class, () -> context.handle(storage),
                "Должно выброситься исключение, если стратегия не установлена");
    }

    @Test
    @DisplayName("ManualFill: считывает данные из консоли и добавляет пользователя")
    void when_executeManualFillWithValidInput_then_userIsAdded() {
        InputStream originalIn = System.in;

        try {
            String simulatedInput = "Ivan\npassword123\nivan@mail.ru\n95\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            EntityStorage<User> storage = new EntityStorage<>();
            Scanner testScanner = new Scanner(System.in);
            PrintStream testOut = new PrintStream(System.out);

            ManualFill<User> manualFill = new ManualFill<>(
                    new User.UserBuilder(), 1, testScanner, testOut
            );

            manualFill.execute(storage);

            assertEquals(1, storage.size(), "Должен быть добавлен 1 пользователь");
            assertEquals("Ivan", storage.get(0).getNickname(), "Имя должно совпадать");
            assertEquals("ivan@mail.ru", storage.get(0).getEmail(), "Email должен совпадать");
            assertEquals(95, storage.get(0).getScores(), "Баллы должны совпадать");

        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    @DisplayName("FileFill: загружает пользователей из файла users.json")
    void when_executeFileFillWithRealUsersJson_then_expectedNumberOfUsersLoaded() {
        EntityStorage<User> storage = new EntityStorage<>();
        ObjectMapper mapper = new ObjectMapper();

        FileFill<User> fileFill = new FileFill<>(mapper, "users.json", User.class);

        fileFill.execute(storage);

        assertEquals(25, storage.size(), "Должно загрузиться 25 валидных пользователей из файла");
    }
}