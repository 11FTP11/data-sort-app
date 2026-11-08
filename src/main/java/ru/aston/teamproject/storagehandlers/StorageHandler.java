package ru.aston.teamproject.storagehandlers;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.ui.IoStreams;

public abstract class StorageHandler<E extends Comparable<E>> {
    protected final EntityStorage<E> storage;
    protected final IoStreams io;

    public StorageHandler(EntityStorage<E> storage, IoStreams io) {
        this.storage = storage;
        this.io = io;
    }

    public abstract void handle();
}
