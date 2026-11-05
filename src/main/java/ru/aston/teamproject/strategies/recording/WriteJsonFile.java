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
    private final String filename;
    private final ObjectMapper mapper;

    public WriteJsonFile(ObjectMapper mapper, String filename) {
        if (filename == null || filename.isBlank()) {
            StringBuilder sb = new StringBuilder();
            sb.append("file_");
            sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")));
            sb.append(".json");

            this.filename = sb.toString();
        } else {
            this.filename = filename;
        }

        this.mapper = mapper;
    }

    @Override
    public void execute(EntityStorage<E> storage) {
        try {
            mapper.writeValue(new File(this.filename), storage);
            System.out.printf("Данные записаны в файл \"%s\"\n", this.filename);
        } catch (DatabindException e) {
            System.err.println("Ошибка структуры данных: " + e.getMessage());
        } catch (StreamWriteException e) {
            System.err.println("Ошибка генерации JSON: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}
