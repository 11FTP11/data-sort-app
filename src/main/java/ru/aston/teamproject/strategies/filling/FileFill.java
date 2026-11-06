package ru.aston.teamproject.strategies.filling;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;

import java.io.File;
import java.io.IOException;
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
        File jsonFile = new File(fileName);
        if (!jsonFile.exists()) {
            throw new RuntimeException("Файл не найден: " + fileName);
        }

        try {
            JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);

            List<E> entities = mapper.readValue(jsonFile, listType);

            if (entities == null || entities.isEmpty()) {
                throw new RuntimeException("Файл пуст или не содержит данных.");
            }

            entities.stream().forEach(storage::add);

            System.out.printf("Загрузка из '%s' завершена. Загружено: %d%n", fileName, entities.size());

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + fileName, e);
        }
    }
}