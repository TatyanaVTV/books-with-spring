package ru.vtvhw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtvhw.dao.BooksDao;
import ru.vtvhw.model.Book;

import java.util.List;

@Service
public class BooksService {

    private BooksDao booksDao;

    @Autowired
    public BooksService(BooksDao booksDao) {
        this.booksDao = booksDao;
    }

    public List<Book> getAllBooks() {
        return booksDao.getAllBooks();
    }

    public Book getBookById(long bookId) {
        return booksDao.getBookById(bookId);
    }
}