package ru.aston.teamproject.sorting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.teamproject.sorting.algorithms.BubbleSort;
import ru.aston.teamproject.sorting.algorithms.SelectionSort;
import ru.aston.teamproject.userbuilder.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {

    private SortContext<User> context;

    @BeforeEach
    void setUp() {
        context = new SortContext<>();
    }

    private List<User> createDefaultUsers() {
        List<User> users = new ArrayList<>();
        users.add(User.builder().setNickname("Zack").setEmail("zack@mail.com").setPassword("pass1").build());
        users.add(User.builder().setNickname("Alice").setEmail("alice@mail.com").setPassword("pass2").build());
        users.add(User.builder().setNickname("Bob").setEmail("bob@mail.com").setPassword("pass3").build());
        return users;
    }

    @Test
    void testBubbleSort_DefaultComparable() {
        context.setStrategy(new BubbleSort<>());
        List<User> users = createDefaultUsers();

        context.handle(users);

        assertEquals("Alice", users.get(0).getNickname());
        assertEquals("Bob", users.get(1).getNickname());
        assertEquals("Zack", users.get(2).getNickname());
    }

    @Test
    void testBubbleSort_CustomComparator() {
        context.setStrategy(new BubbleSort<>());
        List<User> users = createDefaultUsers();

        Comparator<User> byEmailDesc = (u1, u2) -> u2.getEmail().compareTo(u1.getEmail());

        context.handle(users, byEmailDesc);

        assertEquals("zack@mail.com", users.get(0).getEmail());
        assertEquals("bob@mail.com", users.get(1).getEmail());
        assertEquals("alice@mail.com", users.get(2).getEmail());
    }

    @Test
    void testSelectionSort_DefaultComparable() {
        context.setStrategy(new SelectionSort<>());
        List<User> users = createDefaultUsers();

        context.handle(users);

        assertEquals("Alice", users.get(0).getNickname());
        assertEquals("Bob", users.get(1).getNickname());
        assertEquals("Zack", users.get(2).getNickname());
    }

    @Test
    void testSelectionSort_CustomComparator() {
        context.setStrategy(new SelectionSort<>());

        User u1 = User.builder().setNickname("First").setEmail("1@mail.com").setPassword("1").build();
        User u2 = User.builder().setNickname("Second").setEmail("2@mail.com").setPassword("2").build();
        User u3 = User.builder().setNickname("Third").setEmail("3@mail.com").setPassword("3").build();

        List<User> users = new ArrayList<>(List.of(u1, u2, u3));

        Comparator<User> byIdDesc = (a, b) -> Integer.compare(b.getId(), a.getId());
        context.handle(users, byIdDesc);

        assertEquals(u3.getId(), users.get(0).getId());
        assertEquals(u2.getId(), users.get(1).getId());
        assertEquals(u1.getId(), users.get(2).getId());
    }


    @Test
    void testSortWithoutStrategy_ShouldThrowException() {
        List<User> users = createDefaultUsers();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            context.handle(users);
        });

        assertTrue(ex.getMessage().contains("[Exception]: Стратегия сортировки не установлена."));
    }
}