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
import ru.aston.teamproject.ui.IoStreams;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FillTest {

    @Test
    @DisplayName("RandomFill: генерирует указанное количество пользователей")
    void when_executeRandomFillWithCount_then_storageSizeEqualsCount() {
        EntityStorage<User> storage = new EntityStorage<>();
        RandomFill<User> randomFill = new RandomFill<>(new User.UserBuilder(), 5, System.out);

        randomFill.execute(storage);

        assertEquals(5, storage.size(), "Должно быть сгенерировано 5 пользователей");
    }

    @Test
    @DisplayName("EntityContext: вызывает стратегию и наполняет коллекцию")
    void when_contextHandlesWithStrategy_then_strategyIsInvokedAndStorageFilled() {
        EntityStorage<User> storage = new EntityStorage<>();
        EntityContext<User> context = new EntityContext<>();

        context.setStrategy(new RandomFill<>(new User.UserBuilder(), 3, System.out));
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
        String simulatedInput = "Ivan\npassword123\nivan@mail.ru\n95\n";

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream testOutBytes = new ByteArrayOutputStream();

        IoStreams testIo = new IoStreams(
                new Scanner(testIn),
                new PrintStream(testOutBytes),
                new PrintStream(testOutBytes));

        EntityStorage<User> storage = new EntityStorage<>();

        ManualFill<User> manualFill = new ManualFill<>(
                new User.UserBuilder(), 1, testIo);

        manualFill.execute(storage);

        assertEquals(1, storage.size(), "Должен быть добавлен 1 пользователь");
        assertEquals("Ivan", storage.get(0).getNickname(), "Имя должно совпадать");
        assertEquals("ivan@mail.ru", storage.get(0).getEmail(), "Email должен совпадать");
        assertEquals(95, storage.get(0).getScores(), "Баллы должны совпадать");
    }

    @Test
    @DisplayName("FileFill: загружает пользователей из файла users.json")
    void when_executeFileFillWithRealUsersJson_then_expectedNumberOfUsersLoaded() {
        EntityStorage<User> storage = new EntityStorage<>();
        ObjectMapper mapper = new ObjectMapper();

        FileFill<User> fileFill = new FileFill<>(mapper, "users.json", User.class, new IoStreams());

        fileFill.execute(storage);

        assertEquals(25, storage.size(), "Должно загрузиться 25 валидных пользователей из файла");
    }

    @Test
    @DisplayName("FileFill: загружает данные из файла на диске (абсолютный путь)")
    void when_executeFileFillWithTempFileOnDisk_then_usersAreLoaded() throws IOException {

        File tempFile = File.createTempFile("test_users", ".json");
        tempFile.deleteOnExit();

        String jsonContent = """
                [
                  {
                    "nickname": "Fedor",
                    "password": "diskpass123",
                    "email": "disk@mail.ru",
                    "scores": 80
                  }
                ]
                """;
        Files.writeString(tempFile.toPath(), jsonContent);

        EntityStorage<User> storage = new EntityStorage<>();
        ObjectMapper mapper = new ObjectMapper();

        FileFill<User> fileFill = new FileFill<>(mapper, tempFile.getAbsolutePath(), User.class, new IoStreams());

        fileFill.execute(storage);

        assertEquals(1, storage.size(), "Должен загрузиться 1 пользователь с диска");
        assertEquals("Fedor", storage.get(0).getNickname(), "Имя пользователя должно совпадать");
        assertEquals(80, storage.get(0).getScores(), "Баллы должны совпадать");
    }
}
