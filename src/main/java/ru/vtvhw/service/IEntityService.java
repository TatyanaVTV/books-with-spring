package ru.vtvhw.service;

import java.util.List;

public interface IEntityService<T> {
    List<T> findAll();
    T findById(long entityId);
    T save(T entity);
    void delete(T entity);
    void deleteById(long entityId);
}