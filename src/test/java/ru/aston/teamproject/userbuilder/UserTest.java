package ru.aston.teamproject.userbuilder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testCorrectCreation() {
        User user = User.builder()
                .setNickname("alice")
                .setPassword("secret123")
                .setEmail("alice@example.com")
                .build();

        assertEquals("alice", user.getNickname());
        assertEquals("secret123", user.getPassword());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals(1, user.getId());
    }

    @Test
    void testNullNicknameThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname(null).setPassword("pass").setEmail("a@b.com").build()
        );
        assertTrue(ex.getMessage().contains("Nickname"));
    }

    @Test
    void testEmptyPasswordThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname("bob").setPassword("").setEmail("b@c.com").build()
        );
        assertTrue(ex.getMessage().contains("Password"));
    }

    @Test
    void testBlankEmailThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname("carol").setPassword("pwd").setEmail("   ").build()
        );
        assertTrue(ex.getMessage().contains("Email"));
    }

    @Test
    void testInvalidEmailFormatThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            User.builder().setNickname("dave").setPassword("pwd").setEmail("invalid-email").build()
        );
        assertTrue(ex.getMessage().contains("Invalid email format"));
    }

    @Test
    void testChainingReturnsSameBuilder() {
        User.UserBuilder builder = User.builder();
        assertSame(builder, builder.setNickname("test"));
        assertSame(builder, builder.setPassword("test"));
        assertSame(builder, builder.setEmail("test@test.com"));
    }

    @Test
    void testAutoIncrementId() {
        User u1 = User.builder().setNickname("u1").setPassword("p").setEmail("u1@x.com").build();
        User u2 = User.builder().setNickname("u2").setPassword("p").setEmail("u2@x.com").build();
        assertTrue(u2.getId() > u1.getId());
    }
}
