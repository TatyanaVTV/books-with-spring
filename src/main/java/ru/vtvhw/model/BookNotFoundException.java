package ru.vtvhw.model;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(long bookId) {
        super(String.format("Book not found! Id: %d",  bookId));
    }
}