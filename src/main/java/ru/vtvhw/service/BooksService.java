package ru.vtvhw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtvhw.repo.BooksRepository;
import ru.vtvhw.exceptions.BookNotFoundException;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;

import java.util.List;

@Service
public class BooksService implements IEntityService<Book> {

    private final BooksRepository booksRepository;
    private final BooksLoader booksLoader;

    @Autowired
    public BooksService(BooksRepository booksRepository, BooksLoader booksLoader) {
        this.booksRepository = booksRepository;
        this.booksLoader = booksLoader;
    }

    @Override
    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    @Override
    public Book findById(long bookId) {
        return booksRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @Override
    public Book save(Book book) {
        return booksRepository.save(book);
    }

    @Override
    public void  delete(Book book) {
        booksRepository.delete(book);
    }

    @Override
    public void deleteById(long bookId) {
        booksRepository.deleteById(bookId);
    }

    public void addAuthor(Book book, Author author) {
        if (book == null || author == null) {
            throw new IllegalArgumentException("Book and Author must not be null!");
        }
        book.addAuthor(author);
        author.addBook(book);
        booksRepository.save(book);
    }

    public void removeAuthor(Book book, Author author) {
        if (book == null || author == null) {
            throw new IllegalArgumentException("Book and Author must not be null!");
        }
        author.removeBook(book);
        book.removeAuthor(author);
        booksRepository.save(book);
    }

    public void loadBooks(String fileName) {
        var booksToLoad = booksLoader.loadBooksFromFile(fileName);
        booksRepository.saveAll(booksToLoad);
    }
}