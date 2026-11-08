package ru.aston.teamproject;

import ru.aston.teamproject.storagehandlers.StorageHandler;

import java.util.HashMap;
import java.util.Objects;

public class AppDispatcher {
    private static final int ROUTE_NOT_FOUND = -1;
    private static final int CONTEXT_INVOKE_SUCCESS = 0;

    private HashMap<Integer, StorageHandler<?>> routes = new HashMap<>();

    public int resolve(int command) {
        if (this.routes.containsKey(command)) {
            routes.get(command).handle();
            return CONTEXT_INVOKE_SUCCESS;
        }

        return ROUTE_NOT_FOUND;
    }

    public AppDispatcher linkRoute(int id, StorageHandler<?> logic) {
        if (this.routes.containsKey(id)) {
            throw new IllegalArgumentException(String.format("identifier \"%d\" is already registered", id));
        }

        Objects.requireNonNull(logic, "Context logic cannot be null");

        this.routes.put(id, logic);

        return this;
    }
}
