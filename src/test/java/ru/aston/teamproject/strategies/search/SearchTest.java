package ru.aston.teamproject.strategies.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;
import ru.aston.teamproject.storagehandlers.OutputStorageHandler;
import ru.aston.teamproject.ui.IoStreams;

import java.io.File;
import java.nio.file.Path;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ParallelSearchTest {

    private EntityStorage<Integer> storage;
    private IoStreams testIo;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        storage = new EntityStorage<>();
        int[] data = { 0, 1, 2, 2, 2, 3, 4, 5, 5 };
        for (int num : data) {
            storage.add(num);
        }

        outContent = new ByteArrayOutputStream();
        testIo = new IoStreams(
                new Scanner(""),
                new PrintStream(outContent),
                new PrintStream(outContent));
    }

    @Test
    void when_searchExistingElement_then_findsAllOccurrences() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(
                val -> val.equals(2), null, 2, testIo);
        searchStrategy.execute(storage);
        assertEquals(3, searchStrategy.getCoincidenceCount(),
                "Должно быть найдено 3 вхождения числа 2");
    }

    @Test
    void when_searchNonExistentElement_then_returnsZeroOccurrences() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(
                val -> val.equals(99), null, 4, testIo);
        searchStrategy.execute(storage);
        assertEquals(0, searchStrategy.getCoincidenceCount(),
                "Должно быть найдено 0 вхождений");
    }

    @Test
    void when_searchInEmptyStorage_then_returnsZeroOccurrences() {
        EntityStorage<Integer> emptyStorage = new EntityStorage<>();
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(
                val -> val.equals(1), null, 2, testIo);
        assertDoesNotThrow(() -> searchStrategy.execute(emptyStorage),
                "Обработка пустого хранилища не должна выбрасывать исключений");
        assertEquals(0, searchStrategy.getCoincidenceCount(),
                "Количество вхождений в пустом хранилище должно быть 0");
    }

    @Test
    void when_searchWithNullStorage_then_doesNotThrowException() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(
                val -> val.equals(1), null, 2, testIo);
        assertDoesNotThrow(() -> searchStrategy.execute(null),
                "Обработка null хранилища не должна выбрасывать исключений");
        assertEquals(0, searchStrategy.getCoincidenceCount());
    }

    @Test
    void when_createWithInvalidThreadCount_then_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ParallelSearch<>(val -> true, null, 0, testIo);
        }, "Количество потоков должно быть больше 0");

        assertThrows(IllegalArgumentException.class, () -> {
            new ParallelSearch<>(val -> true, null, -5, testIo);
        }, "Количество потоков должно быть больше 0");
    }

    @Test
    void when_createWithNullPredicate_then_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new ParallelSearch<>(null, null, 2, testIo);
        }, "Предикат поиска не может быть null");
    }

    @Test
    void when_searchWithEntityContext_then_delegatesExecutionCorrectly() {
        ParallelSearch<Integer> strategy = new ParallelSearch<>(
                val -> val.equals(5), null, 2, testIo);
        EntityContext<Integer> context = new EntityContext<>();
        context.setStrategy(strategy);
        assertDoesNotThrow(() -> context.handle(storage),
                "Контекст должен успешно выполнить стратегию поиска");
        assertEquals(2, strategy.getCoincidenceCount(),
                "Стратегия должна найти 2 вхождения числа 5");
    }

    @Test
    void when_searchFirstElement_then_findsCorrectly() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(
                val -> val.equals(0), null, 2, testIo);
        searchStrategy.execute(storage);
        assertEquals(1, searchStrategy.getCoincidenceCount(),
                "Должно быть найдено 1 вхождение числа 0");
    }

    @Test
    void when_searchLastElement_then_findsCorrectly() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(
                val -> val.equals(5), null, 2, testIo);
        searchStrategy.execute(storage);
        assertEquals(2, searchStrategy.getCoincidenceCount(),
                "Должно быть найдено 2 вхождения числа 5");
    }

    @Test
    void when_searchWithMultipleThreads_then_returnsCorrectResult() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(
                val -> val.equals(2), null, 4, testIo);
        searchStrategy.execute(storage);
        assertEquals(3, searchStrategy.getCoincidenceCount(),
                "Многопоточный поиск должен найти все 3 вхождения");
    }

    @Test
    void when_getCoincidenceCountBeforeExecution_then_returnsZero() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(
                val -> val.equals(1), null, 2, testIo);
        assertEquals(0, searchStrategy.getCoincidenceCount(),
                "До выполнения поиска количество вхождений должно быть 0");
    }


    @Test
    void when_searchAndWriteToFile_then_fileSuccessfullyWritten(@TempDir Path tempDir) {
        File tempFile = tempDir.resolve("search_output.json").toFile();

        String simulatedInput = "2\n" + tempFile.getAbsolutePath() + "\n3\n";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        IoStreams testIo = new IoStreams(
                new Scanner(simulatedInput),
                new PrintStream(outContent),
                new PrintStream(outContent)
        );

        EntityStorage<Integer> localStorage = new EntityStorage<>();
        localStorage.add(10);
        localStorage.add(20);
        localStorage.add(10);

        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(
                val -> val.equals(10),
                OutputStorageHandler::new,
                2,
                testIo
        );

        searchStrategy.execute(localStorage);

        assertEquals(2, searchStrategy.getCoincidenceCount(), "Должно быть найдено 2 вхождения");
        assertTrue(tempFile.exists(), "Файл должен быть создан на диске");
        assertTrue(tempFile.length() > 0, "Файл не должен быть пустым");
    }
}

