package com.pigeon.service;

public interface BaseService<T> {

    int trueDelete(T entity);

    void checkUnique(T entity);
}
