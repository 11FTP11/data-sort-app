package ru.aston.teamproject.strategies.recording;

import org.junit.jupiter.api.Test;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.entities.User;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WriteOutputTest {

    @Test
    void when_storageContainsUsers_then_correctlyPrintsFormattedOutput() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        WriteOutput<User> strategy = new WriteOutput<>(printStream);

        User user = User.builder()
                .setNickname("alex_dev")
                .setPassword("securePass123")
                .setEmail("alex@example.com")
                .setScores(95)
                .build();

        EntityStorage<User> storage = new EntityStorage<>();
        storage.add(user);

        strategy.execute(storage);

        String expectedUserString = String.format("info %s:\n - password: %s;\n - email: %s\n - scores: %s\n",
                user.getNickname(), user.getPassword(), user.getEmail(), user.getScores());

        String expected = expectedUserString + System.lineSeparator();

        String actualNormalized = outputStream.toString().replace("\r\n", "\n");
        String expectedNormalized = expected.replace("\r\n", "\n");

        assertEquals(expectedNormalized, actualNormalized);
    }
}
