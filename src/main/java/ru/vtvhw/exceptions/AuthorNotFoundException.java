package ru.vtvhw.exceptions;

import ru.vtvhw.model.Author;

public class AuthorNotFoundException extends EntityNotFoundException {
    public AuthorNotFoundException(long authorId) {
        super(Author.class, authorId);
    }
}