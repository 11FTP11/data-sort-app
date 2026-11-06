package ru.aston.teamproject.strategies.filling;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileFill<E extends Comparable<E>> implements Strategy<E> {
    private final ObjectMapper mapper;
    private final String fileName;
    private final Class<E> clazz;

    public FileFill(ObjectMapper mapper, String fileName, Class<E> clazz) {
        this.mapper = mapper;
        this.fileName = fileName;
        this.clazz = clazz;
    }

    @Override
    public void execute(EntityStorage<E> storage) {
        InputStream inputStream = null;

        try {
            File file = new File(fileName);
            if (file.exists() && file.isFile()) {
                inputStream = new FileInputStream(file);
                System.out.println("Чтение файла с диска: " + file.getAbsolutePath());
            } else {
                inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
                if (inputStream != null) {
                    System.out.println("Чтение файла из ресурсов: " + fileName);
                }
            }

            if (inputStream == null) {
                throw new RuntimeException("Файл не найден ни на диске, ни в ресурсах: " + fileName);
            }

            JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<E> entities = mapper.readValue(inputStream, listType);

            if (entities == null || entities.isEmpty()) {
                throw new RuntimeException("Файл пуст или не содержит данных.");
            }

            entities.stream().forEach(storage::add);
            System.out.printf("Загрузка из '%s' завершена. Загружено: %d%n", fileName, entities.size());

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + fileName, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
        }
    }
}