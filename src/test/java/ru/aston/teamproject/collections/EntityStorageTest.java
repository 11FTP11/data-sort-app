package ru.aston.teamproject.collections;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EntityStorageTest {

    @Test
    void when_addSingleElement_then_sizeIncreasesAndElementIsPresent() {
        EntityStorage<String> storage = new EntityStorage<>();
        storage.add("Entity1");

        assertEquals(1, storage.size());
        assertEquals("Entity1", storage.get(0));
    }

    @Test
    void when_addMultipleElementsBeyondDefaultCapacity_then_listGrowsCorrectly() {
        EntityStorage<Integer> storage = new EntityStorage<>();
        for (int i = 0; i < 15; i++) {
            storage.add(i);
        }

        assertEquals(15, storage.size());
        assertEquals(0, storage.get(0));
        assertEquals(14, storage.get(14));
    }

    @Test
    void when_insertElementAtIndex_then_elementsAreShifted() {
        EntityStorage<String> storage = new EntityStorage<>();
        storage.add("Alpha");
        storage.add("Gamma");

        storage.add(1, "Beta");

        assertEquals(3, storage.size());
        assertEquals("Alpha", storage.get(0));
        assertEquals("Beta", storage.get(1));
        assertEquals("Gamma", storage.get(2));
    }

    @Test
    void when_getByValidIndex_then_returnsCorrectElement() {
        EntityStorage<String> storage = new EntityStorage<>();
        storage.add("First");
        storage.add("Second");

        assertEquals("First", storage.get(0));
        assertEquals("Second", storage.get(1));
    }

    @Test
    void when_getByInvalidIndex_then_throwsIndexOutOfBoundsException() {
        EntityStorage<String> storage = new EntityStorage<>();
        storage.add("Item");

        assertThrows(IndexOutOfBoundsException.class, () -> storage.get(5));
        assertThrows(IndexOutOfBoundsException.class, () -> storage.get(-1));
    }

    @Test
    void when_setElementAtIndex_then_returnsOldElementAndUpdatesValue() {
        EntityStorage<String> storage = new EntityStorage<>();
        storage.add("OldValue");

        String oldElement = storage.set(0, "NewValue");

        assertEquals("OldValue", oldElement);
        assertEquals("NewValue", storage.get(0));
        assertEquals(1, storage.size());
    }

    @Test
    void when_removeElementByIndex_then_returnsRemovedElementAndDecreasesSize() {
        EntityStorage<Integer> storage = new EntityStorage<>();
        storage.add(1);
        storage.add(2);
        storage.add(3);

        int removedElement = storage.remove(1);

        assertEquals(2, removedElement);
        assertEquals(2, storage.size());
        assertEquals(1, storage.get(0));
        assertEquals(3, storage.get(1));
    }

    @Test
    void when_streamElements_then_filtersAndCollectsSuccessfully() {
        EntityStorage<String> storage = new EntityStorage<>();
        storage.add("Apple");
        storage.add("Banana");
        storage.add("Apricot");
        storage.add("Cherry");

        List<String> result = storage.stream()
                .filter(s -> s.startsWith("A"))
                .map(String::toUpperCase)
                .toList();

        assertEquals(2, result.size());
        assertTrue(result.contains("APPLE"));
        assertTrue(result.contains("APRICOT"));
    }
}
