package ru.vtvhw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vtvhw.repo.AuthorsRepository;
import ru.vtvhw.exceptions.AuthorNotFoundException;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorsService implements IEntityService<Author> {

    private final AuthorsRepository authorsRepository;

    @Override
    public List<Author> findAll() {
        return authorsRepository.findAll();
    }

    @Override
    public Author findById(long entityId) {
        return authorsRepository.findById(entityId).orElseThrow(() -> new AuthorNotFoundException(entityId));
    }

    @Override
    public Author save(Author entity) {
        return authorsRepository.save(entity);
    }

    @Override
    public void delete(Author entity) {
        authorsRepository.delete(entity);
    }

    @Override
    public void deleteById(long entityId) {
        authorsRepository.deleteById(entityId);
    }

    public void addBook(Author author, Book book) {
        author.addBook(book);
        book.addAuthor(author);
        authorsRepository.save(author);
    }

    public void removeBook(Author author, Book book) {
        author.removeBook(book);
        book.removeAuthor(author);
        authorsRepository.save(author);
    }
}