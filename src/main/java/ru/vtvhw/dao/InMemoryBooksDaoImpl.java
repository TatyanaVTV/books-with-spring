package ru.vtvhw.dao;

import jakarta.annotation.PostConstruct;
import ru.vtvhw.model.Book;
import ru.vtvhw.model.BookNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryBooksDaoImpl implements BooksDao {
    private final Map<Long, Book> booksIdMap = new ConcurrentHashMap<>();
    private volatile long nextId = 1L;

    @PostConstruct
    private void init() {
        addBook(new Book("Spring в действии", "Крейг Уоллс", "Учебная литература", 543));
        addBook(new Book("Head First. Паттерны проектирования", "Эрик Фримен, Элизабет Робсон", "Учебная литература", 640));
        addBook(new Book("Философия Java", "Брюс Эккель", "Учебная литература", 1168));
        addBook(new Book("Создание микросервисов", "Сэм Ньюмен", "Учебная литература", 624));
    }

    @Override
    public List<Book> getAllBooks() {
        return booksIdMap.values().stream().filter(book -> !book.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findBookById(long bookId) {
        return Optional.ofNullable(booksIdMap.get(bookId));
    }

    @Override
    public Book getBookById(long bookId) throws BookNotFoundException {
        return findBookById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @Override
    public long addBook(Book book) {
        var newBookId = nextId++;
        book.setId(newBookId);
        booksIdMap.put(book.getId(), book);
        return newBookId;
    }

    @Override
    public void deleteBookById(long bookId) {
        var deletedBook = getBookById(bookId);
        deletedBook.setDeleted(true);
    }

    @Override
    public void updateBookById(long bookId, Book book) {
        var bookFromStorage = getBookById(book.getId());
        bookFromStorage.setTitle(book.getTitle());
        bookFromStorage.setAuthor(book.getAuthor());
        bookFromStorage.setGenre(book.getGenre());
        bookFromStorage.setNumberOfPages(book.getNumberOfPages());
    }
}