package ru.vtvhw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtvhw.dao.BooksDao;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;

import java.util.List;

@Service
public class BooksService {

    private final BooksDao booksDao;
    private final BooksLoader booksLoader;

    @Autowired
    public BooksService(BooksDao booksDao, BooksLoader booksLoader) {
        this.booksDao = booksDao;
        this.booksLoader = booksLoader;
    }

    public List<Book> getAllBooks() {
        return booksDao.getAll();
    }

    public Book getEntity(long bookId) {
        return booksDao.get(bookId);
    }

    public void createEntity(Book book) {
        booksDao.save(book);
    }

    public void updateEntity(Book book) {
        booksDao.update(book);
    }

    public void deleteEntity(long bookId) {
        booksDao.delete(bookId);
    }

    public void addAuthor(Book book, Author author) {
        book.addAuthor(author);
        author.addBook(book);
        booksDao.save(book);
    }

    public void removeAuthor(Book book, Author author) {
        author.removeBook(book);
        book.removeAuthor(author);
        booksDao.save(book);
    }

    public void loadBooks(String fileName) {
        var booksToLoad = booksLoader.loadBooksFromFile(fileName);
        booksDao.addBooks(booksToLoad);
    }
}