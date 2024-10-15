package ru.vtvhw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vtvhw.dao.AuthorsDao;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorsService implements IEntityService<Author> {

    private final AuthorsDao authorsDao;

    @Override
    public List<Author> getAllEntities() {
        return authorsDao.getAll();
    }

    @Override
    public Author getEntity(long entityId) {
        return authorsDao.get(entityId);
    }

    @Override
    public void createEntity(Author entity) {
        authorsDao.save(entity);
    }

    @Override
    public void updateEntity(Author entity) {
        authorsDao.update(entity);
    }

    @Override
    public void deleteEntity(Author entity) {
        authorsDao.delete(entity);
    }

    @Override
    public void deleteEntity(long entityId) {
        authorsDao.delete(entityId);
    }

    public void addBook(Author author, Book book) {
        author.addBook(book);
        book.addAuthor(author);
        authorsDao.save(author);
    }

    public void removeBook(Author author, Book book) {
        author.removeBook(book);
        book.removeAuthor(author);
        authorsDao.save(author);
    }
}