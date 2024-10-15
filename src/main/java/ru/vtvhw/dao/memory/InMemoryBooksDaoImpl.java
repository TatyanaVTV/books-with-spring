package ru.vtvhw.dao.memory;

import ru.vtvhw.dao.BooksDao;
import ru.vtvhw.exceptions.BookNotFoundException;
import ru.vtvhw.model.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryBooksDaoImpl implements BooksDao {
    private final Map<Long, Book> booksIdMap = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public List<Book> getAll() {
        return booksIdMap.values().stream()
                .filter(book -> !book.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> find(long bookId) {
        return booksIdMap.values().stream()
                .filter(book -> !book.isDeleted() && book.getId() == bookId)
                .findFirst();
    }

    @Override
    public Book get(long bookId) throws BookNotFoundException {
        return find(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @Override
    public long save(Book book) {
        var newBookId = nextId.getAndIncrement();
        book.setId(newBookId);
        booksIdMap.put(book.getId(), book);
        return book.getId();
    }

    @Override
    public void delete(long bookId) {
        var deletedBook = get(bookId);
        delete(deletedBook);
    }

    @Override
    public void delete(Book book) {
        book.setDeleted(true);
        book.removeBookAssociations();
    }

    @Override
    public void update(Book book) {
        var bookFromStorage = get(book.getId());
        bookFromStorage.setTitle(book.getTitle());
        bookFromStorage.setGenre(book.getGenre());
        bookFromStorage.setNumberOfPages(book.getNumberOfPages());
        bookFromStorage.setDeleted(book.isDeleted());
    }

    @Override
    public void addBooks(List<Book> books) {
        books.forEach(this::save);
    }

    @Override
    public List<Book> getForAuthor(long authorId) {
        return booksIdMap.values()
                .stream()
                .filter(book -> book.getAuthors().stream()
                        .anyMatch(author -> author.getId() == authorId))
                .filter(book -> !book.isDeleted())
                .toList();
    }
}