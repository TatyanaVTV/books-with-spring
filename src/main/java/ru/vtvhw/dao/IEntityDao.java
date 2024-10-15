package ru.vtvhw.dao;

import ru.vtvhw.exceptions.AuthorNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IEntityDao<T> {
    List<T> getAll();
    Optional<T> find(long entityId);
    T get(long authorId) throws AuthorNotFoundException;
    long save(T entity);
    void delete(long entityId);
    void delete(T entity);
    void update(T entity);
}
