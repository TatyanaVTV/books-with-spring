package ru.vtvhw.dao.hibernate;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vtvhw.dao.AuthorsDao;
import ru.vtvhw.exceptions.AuthorNotFoundException;
import ru.vtvhw.model.Author;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateAuthorsDaoImpl implements AuthorsDao {

    @Autowired
    private final SessionFactory sessionFactory;

    @Override
    public List<Author> getAll() {
        return sessionFactory.openSession()
                .createSelectionQuery("from Author where deleted = false", Author.class)
                .getResultList();
    }

    @Override
    public Optional<Author> find(long authorId) {
        try {
            return Optional.ofNullable(get(authorId));
        } catch (AuthorNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public Author get(long authorId) throws AuthorNotFoundException {
        try (var session = sessionFactory.openSession()) {
            var author = session.get(Author.class, authorId);
            if (author == null) {
                throw new AuthorNotFoundException(authorId);
            }
            return author;
        }
    }

    @Override
    public long save(Author author) {
        sessionFactory.inTransaction(session -> {
            session.persist(author);
            session.flush();
        });
        return author.getId();
    }

    @Override
    public void delete(long authorId) {
        sessionFactory.inTransaction(session -> {
            var author = session.get(Author.class, authorId);
            session.remove(author);
            session.flush();
        });
    }

    @Override
    public void delete(Author author) {
        sessionFactory.inTransaction(session -> {
            session.remove(author);
            session.flush();
        });
    }

    @Override
    public void update(Author author) {
        sessionFactory.inTransaction(session -> {
            session.merge(author);
            session.flush();
        });
    }

    @Override
    public List<Author> getForBook(long bookId) {
        try (var session = sessionFactory.openSession()) {
            var queryHQL = "SELECT a FROM Author a JOIN a.books b WHERE b.id = :bookId";
            var query = session.createSelectionQuery(queryHQL, Author.class);
            query.setParameter("bookId", bookId);
            return query.list();
        }
    }
}