package ru.aston.teamproject.sorting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.sorting.algorithms.BubbleSort;
import ru.aston.teamproject.sorting.algorithms.SelectionSort;
import ru.aston.teamproject.entities.User;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {

    private UserSortContext<User> context;

    @BeforeEach
    void setUp() {
        context = new UserSortContext<>();
    }

    private EntityStorage<User> createDefaultUsers() {
        EntityStorage<User> users = new EntityStorage<>();
        users.add(User.builder().setNickname("Zack").setEmail("zack@mail.com").setPassword("pass1").setScores(76).build());
        users.add(User.builder().setNickname("Constantin").setEmail("constantin@mail.com").setPassword("pass8").setScores(12).build());
        users.add(User.builder().setNickname("Alice").setEmail("alice@mail.com").setPassword("pass2").setScores(62).build());
        users.add(User.builder().setNickname("Bob").setEmail("bob@mail.com").setPassword("pass3").setScores(55).build());
        users.add(User.builder().setNickname("Alex").setEmail("alex@mail.com").setPassword("pass4").setScores(96).build());
        users.add(User.builder().setNickname("Peter").setEmail("peter@mail.com").setPassword("pass5").setScores(73).build());
        users.add(User.builder().setNickname("Pol").setEmail("pol@mail.com").setPassword("pass6").setScores(10).build());
        users.add(User.builder().setNickname("John").setEmail("john@mail.com").setPassword("pass7").setScores(51).build());
        return users;
    }

    @Test
    void testBubbleSort_DefaultComparable() {
        context.setStrategy(new BubbleSort<>());

        EntityStorage<User> users = createDefaultUsers();
        context.handle(users);
        assertEquals("Alex", users.get(0).getNickname());
        assertEquals("Alice", users.get(1).getNickname());
        assertEquals("Bob", users.get(2).getNickname());
    }

    @Test
    void testBubbleSort_CustomComparator() {
        EntityStorage<User> users = createDefaultUsers();
        Comparator<User> byEmailDesc = (u1, u2) -> u2.getEmail().compareTo(u1.getEmail());
        context.setStrategy(new BubbleSort<>(byEmailDesc));
        context.handle(users);

        assertEquals("zack@mail.com", users.get(0).getEmail());
        assertEquals("pol@mail.com", users.get(1).getEmail());
        assertEquals("peter@mail.com", users.get(2).getEmail());
    }

    @Test
    void testBubbleSort_EvenOnly() {
        EntityStorage<User> users = createDefaultUsers();
        BubbleSort<User> sorter = new BubbleSort<>();
        sorter.sortEvenOnly(users, Comparator.naturalOrder(), User::getScores);

        assertEquals(55, users.get(3).getScores());
        assertEquals(73, users.get(5).getScores());
        assertEquals(51, users.get(7).getScores());

        assertEquals(12, users.get(0).getScores());
        assertEquals(62, users.get(1).getScores());
        assertEquals(76, users.get(2).getScores());
        assertEquals(96, users.get(4).getScores());
        assertEquals(10, users.get(6).getScores());
    }


    @Test
    void testSelectionSort_DefaultComparable() {
        context.setStrategy(new SelectionSort<>());
        EntityStorage<User> users = createDefaultUsers();
        context.handle(users);
        assertEquals("Alex", users.get(0).getNickname());
        assertEquals("Alice", users.get(1).getNickname());
        assertEquals("Bob", users.get(2).getNickname());
        assertEquals("Zack", users.get(7).getNickname());
    }

    @Test
    void testSelectionSort_CustomComparator() {

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
    void testSelectionSort_EvenOnly() {
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
    void testSortWithoutStrategy_ShouldThrowException() {
        EntityStorage<User> users = createDefaultUsers();
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            context.handle(users);
        });
        assertTrue(ex.getMessage().contains("[Exception]: Стратегия сортировки не установлена."));
    }
}