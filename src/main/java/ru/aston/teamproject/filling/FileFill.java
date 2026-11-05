package ru.aston.teamproject.filling;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.entities.User;
import ru.aston.teamproject.strategies.Strategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileFill implements Strategy<User> {
    private final String fileName;
    private final UserValidator validator = new UserValidator();

    public FileFill(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void execute(EntityStorage<User> entityStorage) {
        File jsonFile = new File(fileName);

        if (!jsonFile.exists()) {
            System.out.println("Файл не найден: " + fileName);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            List<User> users = mapper.readValue(
                    jsonFile,
                    new TypeReference<List<User>>() {
                    }
            );

            if (users == null || users.isEmpty()) {
                System.out.println("Файл пуст или не содержит данных.");
                return;
            }

            long validCount = users.stream()
                    .filter(user -> validator.isValid(user.getNickname(), user.getPassword(), user.getEmail()))
                    .map(user -> User.builder()
                            .setNickname(user.getNickname())
                            .setPassword(user.getPassword())
                            .setEmail(user.getEmail())
                            .build())
                    .peek(entityStorage::add)
                    .count();

            System.out.printf("Загрузка из '%s' завершена. Валидных: %d, Пропущено (битых): %d%n",
                    fileName, validCount, users.size() - validCount);

        } catch (com.fasterxml.jackson.databind.exc.MismatchedInputException e) {
            System.out.println("Ошибка структуры JSON: ожидался массив объектов [...]");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при парсинге JSON: " + e.getMessage());
        }
    }
}