package ru.aston.teamproject.strategies.recording;

import ru.aston.teamproject.collections.EntityStorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.ui.IoStreams;

import java.io.File;
import java.io.PrintStream;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Objects;

public class WriteJsonFile<E extends Comparable<E>> implements Strategy<E> {
    private static final String DEFAULT_DIRECTORY = "output/";
    private final File file;
    private final ObjectMapper mapper;
    private final IoStreams io; 

    public WriteJsonFile(ObjectMapper mapper, String filename, IoStreams io) {
        Objects.requireNonNull(mapper, "ObjectMapper cannot be null");
        Objects.requireNonNull(io, "IoStreams cannot be null");
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
        this.io = io;
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
            this.io.out().printf("Данные записаны в файл \"%s\"\n", this.file);
        } catch (DatabindException e) {
            this.io.err().println("Ошибка структуры данных: " + e.getMessage());
        } catch (StreamWriteException e) {
            this.io.err().println("Ошибка генерации JSON: " + e.getMessage());
        } catch (IOException e) {
            this.io.err().println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}
