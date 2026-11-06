package ru.aston.teamproject.strategies.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.EntityContext;

import static org.junit.jupiter.api.Assertions.*;

class ParallelSearchTest {

    private EntityStorage<Integer> storage;

    @BeforeEach
    void setUp() {
        storage = new EntityStorage<>();
        int[] data = {0, 1, 2, 2, 2, 3, 4, 5, 5};
        for (int num : data) {
            storage.add(num);
        }
    }

    @Test
    void when_searchExistingElement_then_findsAllOccurrences() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(2, 2);
        searchStrategy.execute(storage);
        assertEquals(3, searchStrategy.getCoincidenceCount(),
                "Должно быть найдено 3 вхождения числа 2");
    }

    @Test
    void when_searchNonExistentElement_then_returnsZeroOccurrences() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(99, 4);
        searchStrategy.execute(storage);
        assertEquals(0, searchStrategy.getCoincidenceCount(),
                "Должно быть найдено 0 вхождений");
    }

    @Test
    void when_searchInEmptyStorage_then_returnsZeroOccurrences() {
        EntityStorage<Integer> emptyStorage = new EntityStorage<>();
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(1, 2);
        assertDoesNotThrow(() -> searchStrategy.execute(emptyStorage),
                "Обработка пустого хранилища не должна выбрасывать исключений");
        assertEquals(0, searchStrategy.getCoincidenceCount(),
                "Количество вхождений в пустом хранилище должно быть 0");
    }

    @Test
    void when_searchWithNullStorage_then_doesNotThrowException() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(1, 2);
        assertDoesNotThrow(() -> searchStrategy.execute(null),
                "Обработка null хранилища не должна выбрасывать исключений");
        assertEquals(0, searchStrategy.getCoincidenceCount());
    }

    @Test
    void when_createWithInvalidThreadCount_then_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ParallelSearch<>(1, 0);
        }, "Количество потоков должно быть больше 0");

        assertThrows(IllegalArgumentException.class, () -> {
            new ParallelSearch<>(1, -5);
        }, "Количество потоков должно быть больше 0");
    }

    @Test
    void when_createWithNullSearchElement_then_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ParallelSearch<>(null, 2);
        }, "Искомый элемент не может быть null");
    }

    @Test
    void when_searchWithEntityContext_then_delegatesExecutionCorrectly() {
        ParallelSearch<Integer> strategy = new ParallelSearch<>(5, 2);
        EntityContext<Integer> context = new EntityContext<>();
        context.setStrategy(strategy);
        assertDoesNotThrow(() -> context.handle(storage),
                "Контекст должен успешно выполнить стратегию поиска");
        assertEquals(2, strategy.getCoincidenceCount(),
                "Стратегия должна найти 2 вхождения числа 5");
    }

    @Test
    void when_searchFirstElement_then_findsCorrectly() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(0, 2);
        searchStrategy.execute(storage);
        assertEquals(1, searchStrategy.getCoincidenceCount(),
                "Должно быть найдено 1 вхождение числа 0");
    }

    @Test
    void when_searchLastElement_then_findsCorrectly() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(5, 2);
        searchStrategy.execute(storage);
        assertEquals(2, searchStrategy.getCoincidenceCount(),
                "Должно быть найдено 2 вхождения числа 5");
    }

    @Test
    void when_searchWithMultipleThreads_then_returnsCorrectResult() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(2, 4);
        searchStrategy.execute(storage);
        assertEquals(3, searchStrategy.getCoincidenceCount(),
                "Многопоточный поиск должен найти все 3 вхождения");
    }

    @Test
    void when_getSearchElement_then_returnsCorrectElement() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(42, 2);
        assertEquals(42, searchStrategy.getSearchElement(),
                "Метод должен возвращать искомый элемент");
    }

    @Test
    void when_getCoincidenceCountBeforeExecution_then_returnsZero() {
        ParallelSearch<Integer> searchStrategy = new ParallelSearch<>(1, 2);
        assertEquals(0, searchStrategy.getCoincidenceCount(),
                "До выполнения поиска количество вхождений должно быть 0");
    }
}