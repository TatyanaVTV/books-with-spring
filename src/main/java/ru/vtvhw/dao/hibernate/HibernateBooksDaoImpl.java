package ru.vtvhw.dao.hibernate;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vtvhw.dao.BooksDao;
import ru.vtvhw.exceptions.BookNotFoundException;
import ru.vtvhw.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class HibernateBooksDaoImpl implements BooksDao {

    @Autowired
    private final SessionFactory sessionFactory;

    private final int BATCH_SIZE;

    @Override
    public List<Book> getAll() {
        return sessionFactory.openSession()
                .createSelectionQuery("from Book where deleted = false", Book.class)
                .getResultList();
    }

    @Override
    public Optional<Book> find(long bookId) {
        try {
            return Optional.ofNullable(get(bookId));
        } catch (BookNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public Book get(long bookId) throws BookNotFoundException {
        try (var session = sessionFactory.openSession()) {
            var book = session.get(Book.class, bookId);
            if (book == null) {
                throw new BookNotFoundException(bookId);
            }
            return book;
        }
    }


    @Override
    public long save(Book book) {
        sessionFactory.inTransaction(session -> {
            session.persist(book);
            session.flush();
        });
        return book.getId();
    }

    @Override
    public void delete(long bookId) {
        sessionFactory.inTransaction(session -> {
            var author = session.get(Book.class, bookId);
            session.remove(author);
            session.flush();
        });
    }

    @Override
    public void delete(Book book) {
        sessionFactory.inTransaction(session -> {
            var author = session.get(Book.class, book.getId());
            session.remove(author);
            session.flush();
        });
    }

    @Override
    public void update(Book book) {
        sessionFactory.inTransaction(session -> {
            session.merge(book);
            session.flush();
        });
    }

    @Override
    public void addBooks(List<Book> books) {
        AtomicInteger persistedCount = new AtomicInteger(0);
        sessionFactory.inTransaction(session -> {
            books.forEach(book -> {
                if (persistedCount.get() > 0 && persistedCount.get() % BATCH_SIZE == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(book);
            });
            session.flush();
        });
    }

    @Override
    public List<Book> getForAuthor(long authorId) {
        try (var session = sessionFactory.openSession()) {
            var queryHQL = "SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId";
            var query = session.createSelectionQuery(queryHQL, Book.class);
            query.setParameter("authorId", authorId);
            return query.list();
        }
    }
}