package ru.aston.teamproject.contexts;

import ru.aston.teamproject.collections.EntityStorage;
import ru.aston.teamproject.strategies.Strategy;
import ru.aston.teamproject.strategies.search.SearchStrategy;


//заменить этим Context или докинуть. Не до конца понимаю, как связать существующее и это

public class SearchContext<E extends Comparable<E>> implements Context<E> {

    private SearchStrategy<E> strategy;

    public SearchContext(SearchStrategy<E> strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Сратегия поиска не может быть null");
        }
        this.strategy = strategy;
    }

    public SearchContext() {
    }

    @Override //устанавливаем стратегию поиска
    public void setStrategy(Strategy<E> strategy) {
        if (!(strategy instanceof SearchStrategy)) {
            throw new IllegalArgumentException(
                    "Стратегия поиска должна быть выбрана из существующих:  " +
                            (strategy != null ? strategy.getClass().getSimpleName() : "null")
            );
        }
        this.strategy = (SearchStrategy<E>) strategy;
    }

    //из Context
    @Override
    public void handle(EntityStorage<E> storage) {
        if (strategy == null) {
            throw new IllegalStateException(
                    "Стратегия поиска не установлена.."
            );
        }

        if (storage == null) {
            throw new IllegalArgumentException("EntityStorage обязано существовать");
        }

        strategy.execute(storage);
    }


    public SearchStrategy<E> getStrategy() {
        return strategy;
    }
}