package ru.aston.teamproject.collections;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Objects;

class EntityStorage<E> extends AbstractList<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    
    private Object[] data;
    private int size = 0;
    
    public EntityStorage() {
        this.data = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    private Object[] grow(int minCapacity) {
        int oldCapacity = this.data.length;
        if (oldCapacity > 0 || data != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            int newCapacity = Math.max(minCapacity, oldCapacity + (oldCapacity >> 1) + 1);
            return this.data = Arrays.copyOf(this.data, newCapacity);
        } else {
            return this.data = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }
    
    @Override
    public boolean add(E e) {
        add(e, this.data, this.size());
        return true;
    }
    
    private void add(E el, Object[] arrRef, int size) {
        if (size == arrRef.length)
            arrRef = grow();

        arrRef[size] = el;
        this.size = this.size + 1;
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(index);
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        Object[] data;
        int size;

        if ((size = this.size) == (data = this.data).length)
            data = grow();

        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        this.size = size + 1;
    }

    @Override
    public E set(int index, E element) {
        Objects.checkIndex(index, this.size);
        E old = accessElement(index);
        this.data[index] = element;
        
        return old;
    }

    @Override
    public E remove(int index) {
        Object[] data = this.data;
        int size = this.size;

        Objects.checkIndex(index, size);
        E old = accessElement(index);
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null;
        this.size = size;
        
        return old;
    }

    @SuppressWarnings("unchecked")
    private E accessElement(int index) {
        return (E) this.data[index];
    }
    
    @Override
    public E get(int index) {
        Objects.checkIndex(index, this.size);
        return accessElement(index);
    }
    
    private Object[] grow() {
        return grow(this.size + 1);
    }
}
