package ru.vtvhw.dao;

import ru.vtvhw.model.Author;

import java.util.List;

public interface AuthorsDao extends IEntityDao<Author> {
    List<Author> getForBook(long bookId);
}