package ru.vtvhw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.vtvhw.config.BooksAppConfig;
import ru.vtvhw.model.Book;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BooksDao}.
 * Аннотация @Sql подтягивает SQL-скрипт books-db.sql, который будет применен к базе перед выполнением теста.
 * Contact.sql создает таблицу CONTACT с полями (ID, NAME, SURNAME, EMAIL, PHONE_NUMBER) и вставляет в нее 2 записи.
 * Тесты проверяют корректность реализации ContactDao.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BooksAppConfig.class)
@Sql("classpath:/db/books-db.sql")
public record BooksDaoTest(@Autowired DataSource dataSource, @Autowired BooksDao booksDao) {

    private static final Book SPRING_IN_ACTION =
            new Book(1L,
                    "Spring в действии",
                    "Крейг Уоллс",
                    "Учебная литература",
                    543,
                    false);

    private static final Book DESIGN_PATTERNS =
            new Book(2L,
                    "Head First. Паттерны проектирования",
                    "Эрик Фримен, Элизабет Робсон",
                    "Учебная литература",
                    640,
                    false);

    private static final Book THE_PHILOSOPHY_OF_JAVA =
            new Book(3L,
                    "Философия Java",
                    "Брюс Эккель",
                    "Учебная литература",
                    1168,
                    false);

    private static final Book BUILDING_MICROSERVICES =
            new Book(4L,
                    "Создание микросервисов",
                    "Сэм Ньюмен",
                    "Учебная литература",
                    624,
                    false);

    private static final Book DELETED_BOOK =
            new Book(5,
                    "Удаленная книга",
                    "Автор неизвестен",
                    "Жанр неизвестен",
                    100,
                    true);

    /**
     * There are four NOT deleted books inserted in the database in contact.sql.
     */
    private static final List<Book> PERSISTED_BOOKS =
            List.of(SPRING_IN_ACTION, DESIGN_PATTERNS, THE_PHILOSOPHY_OF_JAVA, BUILDING_MICROSERVICES);

    @Test
    void addBook() {
        var book = new Book("Новая тестовая книга", "Неизвестный автор", "Неизвестный жанр", 200);
        var bookId = booksDao.addBook(book);
        book.setId(bookId);

        var bookInDb = booksDao.getBookById(bookId);

        assertThat(bookInDb).isEqualTo(book);
    }

    @Test
    void getBookById() {
        var book = booksDao.getBookById(DESIGN_PATTERNS.getId());

        assertThat(book).isEqualTo(DESIGN_PATTERNS);
    }

    @Test
    void getAllBooks() {
        var books = booksDao.getAllBooks();

        assertThat(books).containsAll(PERSISTED_BOOKS);
        assertThat(books).doesNotContain(DELETED_BOOK);
    }

    @Test
    void findExistingBook() {
        var existingBook = booksDao.findBookById(BUILDING_MICROSERVICES.getId());

        assertThat(existingBook).isEqualTo(Optional.of(BUILDING_MICROSERVICES));
    }

    @Test
    void findNotExistingBook() {
        var notExistingBook = booksDao.findBookById(999);
        assertThat(notExistingBook).isEqualTo(Optional.empty());
    }

    @Test
    void updateBookById() {
        var book = new Book("Тестовая книга", "Неизвестный автор", "Неизвестный жанр", 200);
        var bookId = booksDao.addBook(book);
        book.setId(bookId);

        var bookInDb = booksDao.getBookById(bookId);
        assertThat(bookInDb).isEqualTo(book);

        var newTitle = "Test book";
        var newAuthor = "Test author";
        var newGenre = "Unknown genre";
        var newNumberOfPages = 300;

        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setGenre(newGenre);
        book.setNumberOfPages(newNumberOfPages);

        booksDao.updateBookById(bookId, book);
        bookInDb = booksDao.getBookById(bookId);

        assertThat(bookInDb.getTitle()).isEqualTo(newTitle);
        assertThat(bookInDb.getAuthor()).isEqualTo(newAuthor);
        assertThat(bookInDb.getGenre()).isEqualTo(newGenre);
        assertThat(bookInDb.getNumberOfPages()).isEqualTo(newNumberOfPages);
    }

    @Test
    void deleteBookById() {
        var book = new Book("Книга для удаления", "Автор неизвестен", "Жан неизвестен", 200);
        var bookId = booksDao.addBook(book);

        assertFalse(booksDao.getBookById(bookId).isDeleted());
        booksDao.deleteBookById(bookId);
        assertTrue(booksDao.getBookById(bookId).isDeleted());
    }
}