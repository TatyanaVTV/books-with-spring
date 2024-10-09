package ru.vtvhw.model;

import java.util.Objects;

public class Book {
    private long id;
    private String title;
    private String author;
    private String genre;
    private int numberOfPages;
    private boolean deleted;

    public Book() {
        this("Unknown book", "Unknown author", "Unknown genre", 0);
    }

    public Book(String title, String author, String genre, int numberOfPages) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.numberOfPages = numberOfPages;
        this.deleted = false;
    }

    public Book(long id, String title, String author, String genre, int numberOfPages, boolean deleted) {
        this(title, author, genre, numberOfPages);
        setId(id);
        setDeleted(deleted);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && numberOfPages == book.numberOfPages && deleted == book.deleted && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, genre, numberOfPages, deleted);
    }
}