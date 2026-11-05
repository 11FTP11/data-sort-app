package ru.aston.teamproject.filling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.FillContext;
import ru.aston.teamproject.entities.User;

import static org.junit.jupiter.api.Assertions.*;

class FillTest {

    @Test
    @DisplayName("RandomFill: генерирует указанное количество пользователей")
    void when_executeRandomFillWithCount_then_storageSizeEqualsCount() {
        EntityStorage<User> storage = new EntityStorage<>();
        RandomFill randomFill = new RandomFill(5);

        randomFill.execute(storage);

        assertEquals(5, storage.size(), "Должно быть сгенерировано 5 пользователей");
    }

    @Test
    @DisplayName("FillContext: вызывает стратегию и наполняет коллекцию")
    void when_fillContextHandlesWithStrategy_then_strategyIsInvokedAndStorageFilled() {
        EntityStorage<User> storage = new EntityStorage<>();
        FillContext<User> context = new FillContext<>();

        context.setStrategy(new RandomFill(3));
        context.handle(storage);

        assertEquals(3, storage.size(), "FillContext должен наполнить коллекцию 3 элементами");
    }

    @Test
    @DisplayName("FillContext: выбрасывает исключение без стратегии")
    void when_fillContextHandlesWithoutStrategy_then_throwsIllegalStateException() {
        FillContext<User> context = new FillContext<>();
        EntityStorage<User> storage = new EntityStorage<>();

        assertThrows(IllegalStateException.class, () -> context.handle(storage),
                "Должно выброситься исключение, если стратегия не установлена");
    }

    @Test
    @DisplayName("FileFill: загружает реальный файл users.json из корня проекта")
    void when_executeFileFillWithRealUsersJson_then_expectedNumberOfUsersLoaded() {
        EntityStorage<User> storage = new EntityStorage<>();

        FileFill fileFill = new FileFill("users.json");
        fileFill.execute(storage);

        assertEquals(25, storage.size(), "Должно загрузиться 25 валидных пользователей из реального файла");
    }

    @Test
    @DisplayName("ManualFill: успешно считывает данные из консоли и добавляет пользователя")
    void when_executeManualFillWithValidInput_then_userIsAdded() {
        java.io.InputStream originalIn = System.in;

        try {
            String simulatedInput = "Ivan\npassword123\nivan@mail.ru\n";
            System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

            EntityStorage<User> storage = new EntityStorage<>();
            ManualFill manualFill = new ManualFill(1);
            manualFill.execute(storage);

            assertEquals(1, storage.size(), "Должен быть добавлен 1 пользователь");
            assertEquals("Ivan", storage.get(0).getNickname(), "Имя должно совпадать");
            assertEquals("ivan@mail.ru", storage.get(0).getEmail(), "Email должен совпадать");

        } finally {
            System.setIn(originalIn);
        }
    }
}