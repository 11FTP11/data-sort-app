package ru.aston.teamproject.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.entities.User;
import ru.aston.teamproject.strategies.filling.ManualFill;
import ru.aston.teamproject.strategies.filling.RandomFill;
import ru.aston.teamproject.strategies.recording.WriteJsonFile;
import ru.aston.teamproject.strategies.search.ParallelSearch;
import ru.aston.teamproject.strategies.sorting.algorithms.QuickSort;
import ru.aston.teamproject.ui.IoStreams;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {
    @Test
    void when_fillRandomDataAndSortingScenario_then_storageIsSortiedCorrectlyNameAscScoresDesc() {
        EntityStorage<User> users = new EntityStorage<>();
        EntityContext<User> context = new EntityContext<>();

        context.setStrategy(new RandomFill<>(new User.UserBuilder(), 20, System.out));
        context.handle(users);

        Comparator<User> complexComparator = Comparator
                .comparing(User::getNickname)
                .thenComparing(
                        Comparator.comparingInt(User::getScores).reversed());

        context.setStrategy(new QuickSort<>(complexComparator));
        context.handle(users);

        assertEquals(20, users.size(), "В хранилище должно быть ровно 20 элементов");
        for (Object obj : users) {
            assertInstanceOf(User.class, obj, "Все элементы в хранилище должны быть типа User");
        }

        for (int i = 0; i < users.size() - 1; i++) {
            User current = users.get(i);
            User next = users.get(i + 1);

            int nameComparison = current.getNickname().compareTo(next.getNickname());

            if (nameComparison > 0) {
                fail("Нарушен порядок никнеймов: " + current.getNickname() + " стоит перед " + next.getNickname());
            }

            if (nameComparison == 0) {
                if (current.getScores() < next.getScores()) {
                    fail("Нарушен порядок очков для " + current.getNickname() +
                            ": " + current.getScores() + " стоит перед " + next.getScores());
                }
            }
        }
    }

    @Test
    @DisplayName("Запись вручную записанных данных в файл и поиск одинаковых элементов")
    void when_writeTenUsersInJsonAndSearchRecordWithTreads_then_countRecordsInCollections() throws IOException {
        InputStream inStream = System.in;
        ObjectMapper mapper = new ObjectMapper();
        File tempFile = File.createTempFile("test_user", ".json");
        tempFile.deleteOnExit();

        try {
            String simulatedInput =
                    "Ivan\npassword123\nivan@mail.ru\n95\n" +
                            "Alice\npassword123\nalice@mail.ru\n88\n" +
                            "Bob\npassword123\nbob@mail.ru\n72\n" +
                            "Ivan\npassword123\nivan@mail.ru\n95\n" +
                            "Charlie\npassword123\ncharlie@mail.ru\n85\n" +
                            "Diana\npassword123\ndiana@mail.ru\n91\n" +
                            "Ivan\npassword123\nivan@mail.ru\n95\n" +
                            "Eve\npassword123\neve@mail.ru\n77\n" +
                            "Frank\npassword123\nfrank@mail.ru\n82\n" +
                            "Grace\npassword123\ngrace@mail.ru\n99\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

            ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream testOutBytes = new ByteArrayOutputStream();
            IoStreams testIo = new IoStreams(
                    new Scanner(testIn),
                    new PrintStream(testOutBytes),
                    new PrintStream(testOutBytes));

            EntityStorage<User> users = new EntityStorage<>();

            ManualFill<User> manualFill = new ManualFill<>(
                    new User.UserBuilder(), 10, testIo
            );

            manualFill.execute(users);

            WriteJsonFile<User> strategy = new WriteJsonFile<>(mapper, tempFile.getAbsolutePath(), testIo);
            strategy.execute(users);
            User ivan = User.builder()
                    .setNickname("Ivan")
                    .setEmail("ivan@mail.ru")
                    .setPassword("password123")
                    .setScores(95)
                    .build();

            ParallelSearch<User> search = new ParallelSearch<>(val -> val.equals(ivan), null, 2, testIo);
            search.execute(users);
            assertEquals(10, users.size(), "Должено быть добавлено 10 пользователей");
            assertTrue(tempFile.length() > 0, "Файл отсутствует");
            assertEquals(3, search.getCoincidenceCount(), "Должно быть найдено ровно 3 одинаковых элемента (Ивана)");
        } finally {
            System.setIn(inStream);
        }
    }
}
