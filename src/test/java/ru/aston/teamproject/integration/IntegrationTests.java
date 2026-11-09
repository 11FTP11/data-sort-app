package ru.aston.teamproject.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.entities.User;
import ru.aston.teamproject.storagehandlers.*;
import ru.aston.teamproject.strategies.filling.ManualFill;
import ru.aston.teamproject.strategies.filling.RandomFill;
import ru.aston.teamproject.strategies.recording.WriteJsonFile;
import ru.aston.teamproject.strategies.search.ParallelSearch;
import ru.aston.teamproject.strategies.sorting.SortField;
import ru.aston.teamproject.strategies.sorting.algorithms.QuickSort;
import ru.aston.teamproject.strategies.sorting.algorithms.SelectionSort;
import ru.aston.teamproject.ui.IoStreams;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {
    @Test
    @DisplayName("Создание 20 рандомных записей, сортировка по никнейму, и обратная сортировка по очкам у однофамильцев")
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

    @Test
    @DisplayName("Чтение из файла, сортировка записей по четным scores")
    void when_readFromJsonEvenOnlySorted_then_evenOnlySortedCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("users.json")) {
            assertNotNull(is, "Файл users.json отсутствует в ресурсах");


            EntityStorage<User> users = new EntityStorage<>();
            users.addAll(Arrays.asList(mapper.readValue(is, User[].class)));
            assertFalse(users.isEmpty(), "Коллекция не должна быть пустой после чтения");
            assertEquals(25, users.size(), "В коллекции должно быть 25 элементов");

            SelectionSort<User> sorter = new SelectionSort<>();
            sorter.sortEvenOnly(users, Comparator.naturalOrder(), User::getScores);

            int lastEven = -1;
            for (User u : users) {
                if ((u.getScores() & 1) == 0) {
                    assertTrue(u.getScores() >= lastEven,
                            "Четные значения должны идти по возрастанию! Нарушение на: " + u.getScores());
                    lastEven = u.getScores();
                }
            }
            assertEquals(25, users.size(), "Сортировка не должна менять размер коллекции");
        }
    }

    @Test
    @DisplayName("Добавление пользоваетелей (10), 2 одинаковых, сортировка по нику и поиск")
    void when_inputUsersSortAndSearch_then_elementIsFound() {
        EntityStorage<User> users = new EntityStorage<>();

        String simulatedInput = "3\n10\n" +
                "Alice\npassA\nalice@mail.ru\n85\n" +
                "Ivan\npass1\nivan@mail.ru\n95\n" +
                "Bob\npassB\nbob@mail.ru\n72\n" +
                "Charlie\npassC\ncharlie@mail.ru\n91\n" +
                "Diana\npassD\ndiana@mail.ru\n88\n" +
                "Eve\npassE\neve@mail.ru\n77\n" +
                "Ivan\npass1\nivan@mail.ru\n95\n" +
                "Frank\npassF\nfrank@mail.ru\n82\n" +
                "Grace\npassG\ngrace@mail.ru\n99\n" +
                "Henry\npassH\nhenry@mail.ru\n60\n";

        Scanner scanner = new Scanner(simulatedInput);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        IoStreams io = new IoStreams(scanner, out, out);

        StorageHandler<User> handler = new FillStorageHandler<>(
                users, io, User.builder(), User.class);

        handler.handle();

        assertEquals(10, users.size());

        simulatedInput = "3\n1\n";
        scanner = new Scanner(simulatedInput);
        io = new IoStreams(scanner, out, out);
        handler = new SortStorageHandler<>(
                users, io,
                List.of(
                        new SortField<>("По никнейму", Comparator.comparing(User::getNickname), false, null),
                        new SortField<>("По паролю", Comparator.comparing(User::getPassword), false, null),
                        new SortField<>("По Email", Comparator.comparing(User::getEmail), false, null),
                        new SortField<>("По баллам", Comparator.comparingInt(User::getScores), true, User::getScores)));
        handler.handle();
        assertEquals("Alice", users.get(0).getNickname());
        assertEquals("Bob", users.get(1).getNickname());

        ByteArrayOutputStream searchOutBytes = new ByteArrayOutputStream();
        PrintStream searchOut = new PrintStream(searchOutBytes);
        simulatedInput = "1\nIvan\npass1\nivan@mail.ru\n95\n";

        Scanner searchScanner = new Scanner(simulatedInput);
        IoStreams searchIo = new IoStreams(searchScanner, searchOut, searchOut);

        handler = new SearchStorageHandler<>(users,
                searchIo, 2,
                (localIo) -> {
                    Predicate<User> pred = user -> true;
                    User u = User.builder(false).createManual(localIo.in(), localIo.out());
                    if (!u.getNickname().isBlank()) {
                        pred = pred.and((target) -> target.getNickname().equals(u.getNickname()));
                    }
                    if (!u.getPassword().isBlank()) {
                        pred = pred.and((target) -> target.getPassword().equals(u.getPassword()));
                    }
                    if (!u.getEmail().isBlank()) {
                        pred = pred.and((target) -> target.getEmail().equals(u.getEmail()));
                    }
                    if (u.getScores() != -1) {
                        pred = pred.and((target) -> target.getScores() == u.getScores());
                    }
                    return pred;
                },
                OutputStorageHandler::new);

        handler.handle();

        String searchResult = searchOutBytes.toString();

        assertTrue(searchResult.contains("2"),
                "Поиск должен сообщить о 2 найденных элементах");
    }

    @Test
    @DisplayName("Поиск только по Email (частичные данные) с выводом результатов в консоль")
    void when_searchByEmailOnlyWithOutput_then_elementsArePrinted() {
        EntityStorage<User> users = new EntityStorage<>();

        String simulatedInput = "3\n5\n" +
                "Alice\npassA\nalice@mail.ru\n85\n" +
                "Ivan\npass1\nivan@mail.ru\n95\n" +
                "Bob\npassB\nbob@mail.ru\n72\n" +
                "Ivan2\npass2\nivan@mail.ru\n80\n" +
                "John\npassJ\njohn@mail.ru\n60\n";

        Scanner scanner = new Scanner(simulatedInput);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        IoStreams io = new IoStreams(scanner, out, out);

        StorageHandler<User> fillHandler = new FillStorageHandler<>(
                users, io, User.builder(), User.class);
        fillHandler.handle();

        assertEquals(5, users.size(), "Должно быть добавлено 5 пользователей");

        ByteArrayOutputStream searchOutBytes = new ByteArrayOutputStream();
        PrintStream searchOut = new PrintStream(searchOutBytes);

        String searchInput = "\n1\n\n\nivan@mail.ru\n\n";
        Scanner searchScanner = new Scanner(searchInput);
        IoStreams searchIo = new IoStreams(searchScanner, searchOut, searchOut);

        SearchStorageHandler<User> searchHandler = new SearchStorageHandler<>(
                users, searchIo, 2,
                (localIo) -> {
                    Predicate<User> pred = user -> true;

                    User u = User.builder(false).createManual(localIo.in(), localIo.out());
                    if (!u.getNickname().isBlank()) {
                        pred = pred.and((target) -> target.getNickname().equals(u.getNickname()));
                    }
                    if (!u.getPassword().isBlank()) {
                        pred = pred.and((target) -> target.getPassword().equals(u.getPassword()));
                    }
                    if (!u.getEmail().isBlank()) {
                        pred = pred.and((target) -> target.getEmail().equals(u.getEmail()));
                    }
                    if (u.getScores() != -1) {
                        pred = pred.and((target) -> target.getScores() == u.getScores());
                    }
                    return pred;
                },
                OutputStorageHandler::new);

        searchHandler.handle();

        String searchResult = searchOutBytes.toString();

        assertTrue(searchResult.contains("2"),
                "Поиск должен сообщить о 2 найденных элементах");
    }
}
