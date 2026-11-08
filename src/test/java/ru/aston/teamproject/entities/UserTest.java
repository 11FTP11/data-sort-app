package ru.aston.teamproject.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса User и его Builder")
class UserTest {

    @Test
    @DisplayName("Корректное создание пользователя через Builder")
    void when_buildUserWithValidData_then_userIsCreated() {
        User user = User.builder()
                .setNickname("alice")
                .setPassword("secret123")
                .setEmail("alice@example.com")
                .setScores(55)
                .build();

        assertEquals("alice", user.getNickname(), "Никнейм должен совпадать");
        assertEquals("secret123", user.getPassword(), "Пароль должен совпадать");
        assertEquals("alice@example.com", user.getEmail(), "Email должен совпадать");
        assertEquals(55, user.getScores(), "Баллы должны совпадать");
    }

    @Test
    @DisplayName("Выброс исключения при передаче null в nickname")
    void when_setNullNickname_then_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname(null).setPassword("pass").setEmail("a@b.com").build(),
            "Должно быть выброшено исключение IllegalArgumentException"
        );
        assertTrue(ex.getMessage().contains("Nickname"), "Сообщение об ошибке должно содержать 'Nickname'");
    }

    @Test
    @DisplayName("Выброс исключения при передаче пустого пароля")
    void when_setEmptyPassword_then_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname("bob").setPassword("").setEmail("b@c.com").build(),
            "Должно быть выброшено исключение IllegalArgumentException"
        );
        assertTrue(ex.getMessage().contains("Password"), "Сообщение об ошибке должно содержать 'Password'");
    }

    @Test
    @DisplayName("Выброс исключения при передаче email из пробелов")
    void when_setBlankEmail_then_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname("carol").setPassword("pwd").setEmail("   ").build(),
            "Должно быть выброшено исключение IllegalArgumentException"
        );
        assertTrue(ex.getMessage().contains("Email"), "Сообщение об ошибке должно содержать 'Email'");
    }

    @Test
    @DisplayName("Выброс исключения при неверном формате email")
    void when_setInvalidEmailFormat_then_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname("dave").setPassword("pwd").setEmail("invalid-email").build(),
            "Должно быть выброшено исключение IllegalArgumentException"
        );
        assertTrue(ex.getMessage().contains("Invalid email format"), "Сообщение об ошибке должно содержать 'Invalid email format'");
    }

    @Test
    @DisplayName("Проверка цепочки вызовов (Fluent Interface) Builder")
    void when_callBuilderMethodsChained_then_returnsSameBuilderInstance() {
        User.UserBuilder builder = User.builder();
        assertSame(builder, builder.setNickname("test"), "Метод setNickname должен возвращать тот же объект Builder");
        assertSame(builder, builder.setPassword("test"), "Метод setPassword должен возвращать тот же объект Builder");
        assertSame(builder, builder.setEmail("test@test.com"), "Метод setEmail должен возвращать тот же объект Builder");
    }

    @Test
    @DisplayName("Корректная установка баллов (scores) при создании пользователей")
    void when_buildMultipleUsers_then_scoresAreSetCorrectly() {
        User u1 = User.builder().setNickname("u1").setPassword("p").setEmail("u1@x.com").setScores(45).build();
        User u2 = User.builder().setNickname("u2").setPassword("p").setEmail("u2@x.com").setScores(56).build();
        assertTrue(u2.getScores() > u1.getScores(), "Баллы второго пользователя должны быть больше баллов первого");
    }
}