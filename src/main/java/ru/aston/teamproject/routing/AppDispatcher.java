package ru.aston.teamproject.routing;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.contexts.Context;
import java.util.HashMap;
import java.util.Objects;

public class AppDispatcher<E extends Comparable<E>> {
    private static final int ROUTE_NOT_FOUND = -1;
    private static final int CONTEXT_INVOKE_SUCCESS = 0;

    private EntityStorage<E> storage;
    private HashMap<Integer, Context<E>> routes = new HashMap<>();

    public AppDispatcher(EntityStorage<E> storage) {
        this.storage = storage;
    }

    public int resolve(int command) {
        if (this.routes.containsKey(command)) {
            routes.get(command).handle(storage);
            return CONTEXT_INVOKE_SUCCESS;
        }
        
        return ROUTE_NOT_FOUND;
    }

    public AppDispatcher<E> linkRoute(int id, Context<E> logic) {
        if (this.routes.containsKey(id)) {
            throw new IllegalArgumentException(String.format("identifier \"%d\" is alredy registered", id));
        }

        Objects.requireNonNull(logic, "Context logic cannot be null");

        this.routes.put(id, logic);
        
        return this;
    }
}
