package ru.aston.teamproject.userbuilder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса User и его Builder")
public class UserTest {

    @Test
    @DisplayName("Корректное создание пользователя через Builder")
    void testCorrectCreation() {
        User user = User.builder()
                .setNickname("alice")
                .setPassword("secret123")
                .setEmail("alice@example.com")
                .build();

        assertEquals("alice", user.getNickname(), "Никнейм должен совпадать");
        assertEquals("secret123", user.getPassword(), "Пароль должен совпадать");
        assertEquals("alice@example.com", user.getEmail(), "Email должен совпадать");
        assertEquals(1, user.getId(), "ID первого пользователя должен быть равен 1");
    }

    @Test
    @DisplayName("Выброс исключения при передаче null в nickname")
    void testNullNicknameThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname(null).setPassword("pass").setEmail("a@b.com").build(),
            "Должно быть выброшено исключение IllegalArgumentException"
        );
        assertTrue(ex.getMessage().contains("Nickname"), "Сообщение об ошибке должно содержать 'Nickname'");
    }

    @Test
    @DisplayName("Выброс исключения при передаче пустого пароля")
    void testEmptyPasswordThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname("bob").setPassword("").setEmail("b@c.com").build(),
            "Должно быть выброшено исключение IllegalArgumentException"
        );
        assertTrue(ex.getMessage().contains("Password"), "Сообщение об ошибке должно содержать 'Password'");
    }

    @Test
    @DisplayName("Выброс исключения при передаче email из пробелов")
    void testBlankEmailThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname("carol").setPassword("pwd").setEmail("   ").build(),
            "Должно быть выброшено исключение IllegalArgumentException"
        );
        assertTrue(ex.getMessage().contains("Email"), "Сообщение об ошибке должно содержать 'Email'");
    }

    @Test
    @DisplayName("Выброс исключения при неверном формате email")
    void testInvalidEmailFormatThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname("dave").setPassword("pwd").setEmail("invalid-email").build(),
            "Должно быть выброшено исключение IllegalArgumentException"
        );
        assertTrue(ex.getMessage().contains("Invalid email format"), "Сообщение об ошибке должно содержать 'Invalid email format'");
    }

    @Test
    @DisplayName("Проверка цепочки вызовов (Fluent Interface) Builder")
    void testChainingReturnsSameBuilder() {
        User.UserBuilder builder = User.builder();
        assertSame(builder, builder.setNickname("test"), "Метод setNickname должен возвращать тот же объект Builder");
        assertSame(builder, builder.setPassword("test"), "Метод setPassword должен возвращать тот же объект Builder");
        assertSame(builder, builder.setEmail("test@test.com"), "Метод setEmail должен возвращать тот же объект Builder");
    }

    @Test
    @DisplayName("Автоматическое увеличение ID при создании новых пользователей")
    void testAutoIncrementId() {
        User u1 = User.builder().setNickname("u1").setPassword("p").setEmail("u1@x.com").build();
        User u2 = User.builder().setNickname("u2").setPassword("p").setEmail("u2@x.com").build();
        assertTrue(u2.getId() > u1.getId(), "ID второго пользователя должен быть больше ID первого");
    }
}