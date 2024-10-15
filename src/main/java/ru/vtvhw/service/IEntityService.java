package ru.vtvhw.service;

import java.util.List;

public interface IEntityService<T> {
    List<T> getAllEntities();
    T getEntity(long entityId);
    void createEntity(T entity);
    void updateEntity(T entiry);
    void deleteEntity(T entity);
    void deleteEntity(long entityId);
}
