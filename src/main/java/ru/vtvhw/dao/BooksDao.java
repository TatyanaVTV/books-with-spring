package ru.vtvhw.dao;

import ru.vtvhw.model.Book;

import java.util.List;
import java.util.Optional;

public interface BooksDao {
    List<Book> getAllBooks();
    Optional<Book> findBookById(long id);
    Book getBookById(long id);
    void addBook(Book book);
    void deleteBookById(long id);
    void updateBookById(long id, Book book);
}
