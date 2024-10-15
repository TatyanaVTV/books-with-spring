package ru.vtvhw.dao.memory;

import ru.vtvhw.dao.AuthorsDao;
import ru.vtvhw.exceptions.AuthorNotFoundException;
import ru.vtvhw.model.Author;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryAuthorsDaoImpl implements AuthorsDao {
    private final Map<Long, Author> authorsIdMap = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    @Override
    public List<Author> getAll() {
        return authorsIdMap.values().stream()
                .filter(author -> !author.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Author> find(long authorId) {
        return authorsIdMap.values().stream()
                .filter(author -> !author.isDeleted() && author.getId() == authorId)
                .findFirst();
    }

    @Override
    public Author get(long authorId) throws AuthorNotFoundException {
        return find(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));
    }

    @Override
    public long save(Author author) {
        var newAuthorId = nextId.getAndIncrement();
        author.setId(newAuthorId);
        authorsIdMap.put(author.getId(), author);
        return author.getId();
    }

    @Override
    public void delete(long authorId) {
        var deletedAuthor = get(authorId);
        delete(deletedAuthor);
    }

    @Override
    public void delete(Author author) {
        author.setDeleted(true);
        author.removeBookAssociations();
    }

    @Override
    public void update(Author author) {
        var authorFromStorage = get(author.getId());
        authorFromStorage.setName(author.getName());
        author.setDeleted(author.isDeleted());
    }

    @Override
    public List<Author> getForBook(long bookId) {
        return authorsIdMap.values()
                .stream()
                .filter(author -> author.getBooks().stream()
                        .anyMatch(book -> book.getId() == bookId))
                .filter(author -> !author.isDeleted())
                .toList();
    }
}