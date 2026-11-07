package ru.aston.teamproject.strategies.sorting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.storagehandlers.SortStorageHandler;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.strategies.sorting.algorithms.BubbleSort;
import ru.aston.teamproject.strategies.sorting.algorithms.QuickSort;
import ru.aston.teamproject.strategies.sorting.algorithms.SelectionSort;
import ru.aston.teamproject.entities.User;
import ru.aston.teamproject.strategies.sorting.SortField;
import ru.aston.teamproject.ui.IoStreams;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;

class SortTest {

    private EntityContext<User> context;

    @BeforeEach
    void setUp() {
        context = new EntityContext<>();
    }

    private EntityStorage<User> createDefaultUsers() {
        EntityStorage<User> users = new EntityStorage<>();
        users.add(User.builder().setNickname("Zack").setEmail("zack@mail.com").setPassword("pass1").setScores(76).build());
        users.add(User.builder().setNickname("Constantin").setEmail("rivsmaster@mail.com").setPassword("pass8").setScores(12).build());
        users.add(User.builder().setNickname("Alice").setEmail("hoodwork@mail.com").setPassword("pass2").setScores(62).build());
        users.add(User.builder().setNickname("Bob").setEmail("bob@mail.com").setPassword("pass3").setScores(55).build());
        users.add(User.builder().setNickname("Alex").setEmail("alex@mail.com").setPassword("pass4").setScores(96).build());
        users.add(User.builder().setNickname("Peter").setEmail("peter@mail.com").setPassword("pass5").setScores(73).build());
        users.add(User.builder().setNickname("Pol").setEmail("pol@mail.com").setPassword("pass6").setScores(10).build());
        users.add(User.builder().setNickname("John").setEmail("noobmaster666@mail.com").setPassword("pass7").setScores(51).build());
        return users;
    }

    @Test
    void when_applyBubbleSort_then_listIsSortedByDefault() {
        context.setStrategy(new BubbleSort<>());

        EntityStorage<User> users = createDefaultUsers();
        context.handle(users);
        assertEquals("Alex", users.get(0).getNickname());
        assertEquals("Alice", users.get(1).getNickname());
        assertEquals("Bob", users.get(2).getNickname());
    }

    @Test
    void when_applyBubbleSortWithCustomComparator_then_listIsCustomSorted() {
        EntityStorage<User> users = createDefaultUsers();
        Comparator<User> byEmailDesc = (u1, u2) -> u2.getEmail().compareTo(u1.getEmail());
        context.setStrategy(new BubbleSort<>(byEmailDesc));
        context.handle(users);

        assertEquals("zack@mail.com", users.get(0).getEmail());
        assertEquals("rivsmaster@mail.com", users.get(1).getEmail());
        assertEquals("pol@mail.com", users.get(2).getEmail());
    }

    @Test
    void when_applyBubbleSortEvenOnly_then_onlyEvenValuesAreSorted() {
        EntityStorage<User> users = createDefaultUsers();
        BubbleSort<User> sorter = new BubbleSort<>();
        sorter.sortEvenOnly(users, Comparator.naturalOrder(), User::getScores);

        assertEquals(55, users.get(3).getScores());
        assertEquals(73, users.get(5).getScores());
        assertEquals(51, users.get(7).getScores());

        assertEquals(10, users.get(0).getScores());
        assertEquals(12, users.get(1).getScores());
        assertEquals(62, users.get(2).getScores());
        assertEquals(76, users.get(4).getScores());
        assertEquals(96, users.get(6).getScores());
    }


    @Test
    void when_applySelectionSort_then_listIsSortedByDefault() {
        context.setStrategy(new SelectionSort<>());
        EntityStorage<User> users = createDefaultUsers();
        context.handle(users);
        assertEquals("Alex", users.get(0).getNickname());
        assertEquals("Alice", users.get(1).getNickname());
        assertEquals("Bob", users.get(2).getNickname());
        assertEquals("Zack", users.get(7).getNickname());
    }

    @Test
    void when_applySelectionSortWithCustomComparator_then_listIsCustomSorted() {

        EntityStorage<User> users = createDefaultUsers();

        Comparator<User> byIdDesc = (a, b) -> Integer.compare(b.getScores(), a.getScores());
        context.setStrategy(new SelectionSort<>(byIdDesc));
        context.handle(users);

        assertEquals("Alex", users.get(0).getNickname());
        assertEquals("Zack", users.get(1).getNickname());
        assertEquals("Peter", users.get(2).getNickname());
        assertEquals("Pol", users.get(7).getNickname());
    }

    @Test
    void when_applySelectionSortEvenOnly_then_onlyEvenValuesAreSorted() {
        EntityStorage<User> users = createDefaultUsers();
        SelectionSort<User> sorter = new SelectionSort<>();

        sorter.sortEvenOnly(users, Comparator.naturalOrder(), User::getScores);

        assertEquals(55, users.get(3).getScores());
        assertEquals(73, users.get(5).getScores());
        assertEquals(51, users.get(7).getScores());

        assertEquals(10, users.get(0).getScores());
        assertEquals(12, users.get(1).getScores());
        assertEquals(62, users.get(2).getScores());
        assertEquals(76, users.get(4).getScores());
        assertEquals(96, users.get(6).getScores());
    }

    @Test
    void when_applyQuickSort_then_dataIsSortedByDefault() {
        EntityContext<User> context = new EntityContext<>();
        context.setStrategy(new QuickSort<>());

        EntityStorage<User> users = createDefaultUsers();
        context.handle(users);

        assertEquals("Alex", users.get(0).getNickname());
        assertEquals("Alice", users.get(1).getNickname());
        assertEquals("Bob", users.get(2).getNickname());
        assertEquals("Zack", users.get(7).getNickname());
    }

    @Test
    void when_applyQuickSortWithCustomComparator_then_dataIsCustomSorted() {
        EntityContext<User> context = new EntityContext<>();

        Comparator<User> byEmailDesc = (u1, u2) -> u2.getEmail().compareTo(u1.getEmail());
        context.setStrategy(new QuickSort<>(byEmailDesc));

        EntityStorage<User> users = createDefaultUsers();
        context.handle(users);

        assertEquals("zack@mail.com", users.get(0).getEmail());
        assertEquals("rivsmaster@mail.com", users.get(1).getEmail());
        assertEquals("pol@mail.com", users.get(2).getEmail());
        assertEquals("alex@mail.com", users.get(7).getEmail());
    }


    @Test
    void when_applyQuickSortEvenOnly_then_onlyEvenValuesAreSorted() {
        QuickSort<User> sorter = new QuickSort<>();
        EntityStorage<User> users = createDefaultUsers();

        sorter.sortEvenOnly(users, Comparator.naturalOrder(), User::getScores);

        assertEquals(10, users.get(0).getScores());
        assertEquals(12, users.get(1).getScores());
        assertEquals(62, users.get(2).getScores());
        assertEquals(55, users.get(3).getScores());
        assertEquals(76, users.get(4).getScores());
        assertEquals(73, users.get(5).getScores());
        assertEquals(96, users.get(6).getScores());
        assertEquals(51, users.get(7).getScores());
    }

    @Test
    void when_applySortWithoutStrategy_then_exceptionIsThrown() {
        EntityStorage<User> users = createDefaultUsers();
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            context.handle(users);
        });
        assertTrue(ex.getMessage().contains("Cтратегия не была установлена в контекст"));
    }


    @Test
    void when_sortStorageHandlerStandardSort_then_dataIsSorted() {
        EntityStorage<User> users = createDefaultUsers();

        List<SortField<User>> fields = List.of(
                new SortField<>("По никнейму", Comparator.comparing(User::getNickname), false, null),
                new SortField<>("По Email", Comparator.comparing(User::getEmail), false, null),
                new SortField<>("По баллам", Comparator.comparingInt(User::getScores), true, User::getScores)
        );

        String simulatedInput = "1\n1\n";

        Scanner scanner = new Scanner(simulatedInput);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        IoStreams io = new IoStreams(scanner, out, out);

        SortStorageHandler<User> handler = new SortStorageHandler<>(users, io, fields);
        handler.handle();

        assertEquals("Alex", users.get(0).getNickname());
        assertEquals("Alice", users.get(1).getNickname());
    }
}
