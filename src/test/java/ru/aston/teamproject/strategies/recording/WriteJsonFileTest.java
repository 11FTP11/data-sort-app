package ru.aston.teamproject.strategies.recording;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.entities.User;
import ru.aston.teamproject.ui.IoStreams;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WriteJsonFileTest {

    @Test
    void when_validStorageAndFilename_then_fileSuccessfullyWritten(@TempDir Path tempDir) {
        ObjectMapper mapper = new ObjectMapper();
        
        File tempFile = tempDir.resolve("test_output.json").toFile();
        
        EntityStorage<String> storage = new EntityStorage<>();
        storage.add("Item1");
        storage.add("Item2");

        WriteJsonFile<String> strategy = new WriteJsonFile<>(mapper, tempFile.getAbsolutePath(), new IoStreams());

        strategy.execute(storage);

        assertTrue(tempFile.exists(), "file must be on disk");
        assertTrue(tempFile.length() > 0, "file cannot be empty");
    }

    @Test
    void when_nullProvided_then_generatesDefaultFileWithTimestamp() {

        ObjectMapper mapper = new ObjectMapper();
        
        WriteJsonFile<String> strategy = new WriteJsonFile<>(mapper, null, new IoStreams());
        strategy.execute(null);
        File f = strategy.getFile();
        assertTrue(f.exists(), "file must be on disk");
    }

    @Test
    void when_fileIsDirectory_then_handlesIoExceptionGracefully(@TempDir Path tempDir) {

        ObjectMapper mapper = new ObjectMapper();

        String directoryPath = tempDir.toString();
        WriteJsonFile<String> strategy = new WriteJsonFile<>(mapper, directoryPath, new IoStreams());
     
        assertDoesNotThrow(() -> strategy.execute(null));
    }
    
    
    @Test
    void when_storageContainsUsers_then_fileSuccessfullyWritten(@TempDir Path tempDir) {
        ObjectMapper mapper = new ObjectMapper();
        File tempFile = tempDir.resolve("users_output.json").toFile();

        User user = User.builder()
                .setNickname("alexios")
                .setPassword("securePassword123")
                .setEmail("alex.dev@example.com")
                .setScores(85)
                .build();

        EntityStorage<User> storage = new EntityStorage<>();
        storage.add(user);

        WriteJsonFile<User> strategy = new WriteJsonFile<>(mapper, tempFile.getAbsolutePath(), new IoStreams());

        strategy.execute(storage);
        
        assertTrue(tempFile.exists(), "file must be on disk");
        assertTrue(tempFile.length() > 0, "file cannot be empty");
    }
}
