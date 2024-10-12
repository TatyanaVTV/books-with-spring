package ru.vtvhw.dao;

import ru.vtvhw.model.Book;
import ru.vtvhw.model.BookNotFoundException;

import java.util.List;
import java.util.Optional;

public interface BooksDao {
    List<Book> getAllBooks();
    Optional<Book> findBookById(long bookId);
    Book getBookById(long bookId) throws BookNotFoundException;
    long addBook(Book book);
    void deleteBookById(long bookId);
    void updateBookById(long bookId, Book book);
    void addBooks(List<Book> books);
}