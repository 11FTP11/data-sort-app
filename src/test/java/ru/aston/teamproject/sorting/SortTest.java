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
    void setUp() throws Exception {
        context = new SortContext<>();
        try {
            java.lang.reflect.Field field = User.UserBuilder.class.getDeclaredField("idCounter");
            field.setAccessible(true);
            field.setInt(null, 0);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> createDefaultUsers() {
        List<User> users = new ArrayList<>();
        users.add(User.builder().setNickname("Zack").setEmail("zack@mail.com").setPassword("pass1").build());
        users.add(User.builder().setNickname("Constantin").setEmail("constantin@mail.com").setPassword("pass8").build());
        users.add(User.builder().setNickname("Alice").setEmail("alice@mail.com").setPassword("pass2").build());
        users.add(User.builder().setNickname("Bob").setEmail("bob@mail.com").setPassword("pass3").build());
        users.add(User.builder().setNickname("Alex").setEmail("alex@mail.com").setPassword("pass4").build());
        users.add(User.builder().setNickname("Peter").setEmail("peter@mail.com").setPassword("pass5").build());
        users.add(User.builder().setNickname("Pol").setEmail("pol@mail.com").setPassword("pass6").build());
        users.add(User.builder().setNickname("John").setEmail("john@mail.com").setPassword("pass7").build());
        return users;
    }

    /*!!!!!!!!!!!!!!!ПЕРЕД РЕЛИЗОМ УБРАТЬ ЭТОТ МЕТОД!!!!!!!!!!!!!!!!!!*/
    private static void printUsers(List<User> users) {
        users.forEach(user -> System.out.printf(
                "login:\t%s\t | Email:\t%s\t | id:\t%d%n",
                user.getNickname(),
                user.getEmail(),
                user.getId()
        ));
    }

    @Test
    void testBubbleSort_DefaultComparable() {
        context.setStrategy(new BubbleSort<>());

        List<User> users = createDefaultUsers();
        context.handle(users);
        assertEquals("Alex", users.get(0).getNickname());
        assertEquals("Alice", users.get(1).getNickname());
        assertEquals("Bob", users.get(2).getNickname());
    }

    @Test
    void testBubbleSort_CustomComparator() {
        context.setStrategy(new BubbleSort<>());
        List<User> users = createDefaultUsers();

        Comparator<User> byEmailDesc = (u1, u2) -> u2.getEmail().compareTo(u1.getEmail());

        context.handle(users, byEmailDesc);

        assertEquals("zack@mail.com", users.get(0).getEmail());
        assertEquals("pol@mail.com", users.get(1).getEmail());
        assertEquals("peter@mail.com", users.get(2).getEmail());
    }

    @Test
    void testBubbleSort_EvenOnly() {
        List<User> users = createDefaultUsers();
        BubbleSort<User> sorter = new BubbleSort<>();
        sorter.sort(users); //Сортируем по никнейму, чтобы id были не по порядку.
        sorter.sortEvenOnly(users, User::getId);
        assertEquals(5, users.get(0).getId());
        assertEquals(3, users.get(1).getId());
        assertEquals(2, users.get(2).getId());
        assertEquals(4, users.get(3).getId());
        assertEquals(6, users.get(4).getId());
        assertEquals(8, users.get(5).getId());

    }


    @Test
    void testSelectionSort_DefaultComparable() {
        context.setStrategy(new SelectionSort<>());
        List<User> users = createDefaultUsers();

        context.handle(users);

        assertEquals("Alex", users.get(0).getNickname());
        assertEquals("Alice", users.get(1).getNickname());
        assertEquals("Bob", users.get(2).getNickname());
        assertEquals("Zack", users.get(7).getNickname());
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
    void testSelectionSort_EvenOnly() {
        List<User> users = createDefaultUsers();
        SelectionSort<User> sorter = new SelectionSort<>();

        sorter.sort(users);

        sorter.sortEvenOnly(users, User::getId);

        assertEquals(5, users.get(0).getId());
        assertEquals(3, users.get(1).getId());
        assertEquals(2, users.get(2).getId());
        assertEquals(4, users.get(3).getId());
        assertEquals(6, users.get(4).getId());
        assertEquals(8, users.get(5).getId());
        assertEquals(7, users.get(6).getId());
        assertEquals(1, users.get(7).getId());
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