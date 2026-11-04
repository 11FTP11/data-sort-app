package ru.aston.teamproject.strategies.search;

import ru.aston.teamproject.strategies.Strategy;


//Интерфейс стратегии поиска, расширяет Strategy для операций поиска в коллекции
//<E> должен быть сравнимым

public interface SearchStrategy<E extends Comparable<E>> extends Strategy<E> {

//Поиск и счет вхождений элемента в коллекции, entityStorage- хранилища данных для поиска
    @Override
    void execute(ru.aston.teamproject.collections.EntityStorage<E> entityStorage);

//Возвращает результат поиска- количество совпадений, соответственно, вхождений в коллекцию
    int getCoincndenceCount();


//Возвращает искомый элемент
    E getSearchElement();
}
