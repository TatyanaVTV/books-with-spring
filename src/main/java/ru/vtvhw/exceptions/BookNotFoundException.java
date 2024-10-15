package ru.vtvhw.exceptions;

import ru.vtvhw.model.Book;

public class BookNotFoundException extends EntityNotFoundException {
    public BookNotFoundException(long bookId) {
        super(Book.class, bookId);
    }
}