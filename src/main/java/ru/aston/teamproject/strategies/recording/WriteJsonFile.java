package ru.aston.teamproject.strategies.recording;

import ru.aston.teamproject.collections.EntityStorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import ru.aston.teamproject.strategies.Strategy;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WriteJsonFile<E extends Comparable<E>> implements Strategy<E> {
    private static final String DEFAULT_DIRECTORY = "output/";
    private final File file;
    private final ObjectMapper mapper;

    public WriteJsonFile(ObjectMapper mapper, String filename) {
        if (filename == null || filename.isBlank()) {

            StringBuilder sb = new StringBuilder();
            sb.append(DEFAULT_DIRECTORY);
            sb.append("file_");
            sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")));
            sb.append(".json");

            this.file = new File(sb.toString());
        } else {
            this.file = new File(filename);
        }

        this.mapper = mapper;
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public void execute(EntityStorage<E> storage) {
        File parentDir = this.file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try {
            mapper.writeValue(this.file, storage);
            System.out.printf("Данные записаны в файл \"%s\"\n", this.file);
        } catch (DatabindException e) {
            System.err.println("Ошибка структуры данных: " + e.getMessage());
        } catch (StreamWriteException e) {
            System.err.println("Ошибка генерации JSON: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}
