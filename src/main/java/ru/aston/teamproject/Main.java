package ru.aston.teamproject;

import ru.aston.teamproject.collections.EntityStorage;

import ru.aston.teamproject.entities.User;

import ru.aston.teamproject.strategies.sorting.SortField;

import ru.aston.teamproject.storagehandlers.Description;
import ru.aston.teamproject.storagehandlers.StorageHandler;
import ru.aston.teamproject.storagehandlers.OutputStorageHandler;
import ru.aston.teamproject.storagehandlers.FillStorageHandler;
import ru.aston.teamproject.storagehandlers.SearchStorageHandler;
import ru.aston.teamproject.storagehandlers.SortStorageHandler;

import java.util.function.Predicate;

import java.util.Comparator;
import java.util.List;

import ru.aston.teamproject.ui.App;
import ru.aston.teamproject.ui.IoStreams;

public class Main {
    private static int THREADS_COUNT = 6;

    public static void main(String[] args) {

        EntityStorage<User> userStorage = new EntityStorage<>();
        IoStreams io = new IoStreams();

        StorageHandler<User> outHandler = new OutputStorageHandler<>(
                userStorage,
                io);

        StorageHandler<User> fillHandler = new FillStorageHandler<>(
                userStorage, io, User.builder(), User.class);

        StorageHandler<User> sortHandler = new SortStorageHandler<>(
                userStorage, io,
                List.of(
                        new SortField<>("По никнейму", Comparator.comparing(User::getNickname), false, null),
                        new SortField<>("По Email", Comparator.comparing(User::getEmail), false, null),
                        new SortField<>("По баллам", Comparator.comparingInt(User::getScores), true, User::getScores)));

        StorageHandler<User> searchHandler = new SearchStorageHandler<>(userStorage,
                io, THREADS_COUNT,
                (localIo) -> {
                    Predicate<User> pred = user -> true;
                    User u = User.builder(false).createManual(localIo.in(), localIo.out());
                    if (!u.getNickname().isBlank()) { pred = pred.and((target) -> target.getNickname().equals(u.getNickname())); }
                    if (!u.getPassword().isBlank()) { pred = pred.and((target) -> target.getPassword().equals(u.getPassword())); }
                    if (!u.getEmail().isBlank()) { pred = pred.and((target) -> target.getEmail().equals(u.getEmail())); }
                    if (u.getScores() != -1) { pred = pred.and((target) -> target.getScores() == u.getScores()); }
                    return pred;
                },
                OutputStorageHandler::new);

        App app = new App(io, "СИСТЕМА УПРАВЛЕНИЯ ПОЛЬЗОВАТЕЛЯМИ", new AppDispatcher());
        app.linkAll(
                new Description("Заполнение хранилища", fillHandler),
                new Description("Вывод элементов хранилища", outHandler),
                new Description("Cортировка хранилища", sortHandler),
                new Description("Поиск элемента в хранилище", searchHandler));

        app.start();
    }
}
