package ru.vtvhw.dao;

import ru.vtvhw.model.Book;

import java.util.List;

public interface BooksDao extends IEntityDao<Book> {
    void addBooks(List<Book> books);
    List<Book> getForAuthor(long authorId);
}