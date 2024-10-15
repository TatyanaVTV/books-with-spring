package ru.vtvhw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.vtvhw.config.BooksAppConfig;
import ru.vtvhw.exceptions.BookNotFoundException;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;
import ru.vtvhw.service.BooksLoader;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link BooksDao}.
 * Аннотация @Sql подтягивает SQL-скрипты books-db.sql, authors-db.sql и authors-to-books-db.sql,
 * которыё будут применен к базе перед выполнением тестов.
 * books-db.sql создает таблицу books с полями (book_id, title,  genre, pages, deleted) и вставляет в неё 6 записей.
 * authors-db.sql создает таблицу authors с полями (author_id, author_name, deleted) и вставляет в неё 5 записей.
 * authors-to-books-db.sql создаёт таблицу author_books с полями (author_id, book_id) и вставляет в неё 6 записей.
 * Тесты проверяют корректность реализации BooksDao.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BooksAppConfig.class)
@Sql("classpath:/db/books-db.sql")
@Sql("classpath:/db/authors-db.sql")
@Sql("classpath:/db/authors-to-books-db.sql")
public record BooksDaoTest(@Autowired DataSource dataSource,
                           @Autowired BooksDao booksDao,
                           @Autowired BooksLoader booksLoader) {

    private static final Book SPRING_IN_ACTION =
            new Book(1L, "Spring в действии", "Учебная литература", 543, false);

    private static final Book DESIGN_PATTERNS =
            new Book(2L, "Head First. Паттерны проектирования", "Учебная литература", 640, false);

    private static final Book THE_PHILOSOPHY_OF_JAVA =
            new Book(3L, "Философия Java", "Учебная литература", 1168, false);

    private static final Book BUILDING_MICROSERVICES =
            new Book(4L, "Создание микросервисов", "Учебная литература", 624, false);

    private static final Book DELETED_BOOK =
            new Book(5L, "Удаленная книга", "Жанр неизвестен", 100, true);

    private static final Book MONOLITH_TO_MICROSERVICES =
            new Book(6L, "От монолита к микросервисам", "Учебная литература", 272, false);

    private static final Author CRAIG_WALLS = new Author(1L, "Крейг Уоллс");
    private static final Author SAM_NEWMAN = new Author(5L, "Сэм Ньюмен");

    /**
     * There are four NOT deleted books inserted in the database in /db/books-db.sql.
     */
    private static final List<Book> PERSISTED_BOOKS =
            List.of(SPRING_IN_ACTION, DESIGN_PATTERNS, THE_PHILOSOPHY_OF_JAVA, BUILDING_MICROSERVICES);

    private static final String CORRECT_BOOKS_FILE_NAME = "csv/OneThousandTestBooks.csv";

    @Test
    void save_BookDoesNotExistInRepo_BookInRepoShouldBeEqualToSaved() {
        var book = new Book("Новая тестовая книга", "Неизвестный жанр", 200);
        var bookId = booksDao.save(book);
        book.setId(bookId);

        var bookInDb = booksDao.get(bookId);

        assertThat(bookInDb).isEqualTo(book);
    }

    @Test
    void get_BookExistsInRepo_ShouldBeFoundAndEqualToExpected() {
        var book = booksDao.get(DESIGN_PATTERNS.getId());

        assertThat(book).isEqualTo(DESIGN_PATTERNS);
    }

    @Test
    void get_BookDoesNotExistInRepo_ShouldThrowBookNotFoundException() {
        assertThatThrownBy(() -> booksDao.get(99999))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void getAll_PreLoadedBooks_ShouldReturnListOfAllNotDeletedBooks() {
        var books = booksDao.getAll();

        assertThat(books).containsAll(PERSISTED_BOOKS);
        assertThat(books).doesNotContain(DELETED_BOOK);
    }

    @Test
    void find_BookExistsInRepo_ShouldReturnNotEmptyOptionalOfExpectedBook() {
        var existingBook = booksDao.find(BUILDING_MICROSERVICES.getId());

        assertThat(existingBook).isEqualTo(Optional.of(BUILDING_MICROSERVICES));
    }

    @Test
    void find_BookDoesNotExistInRepo_ShouldReturnEmptyOptional() {
        var notExistingBook = booksDao.find(99999);
        assertThat(notExistingBook).isEqualTo(Optional.empty());
    }

    @Test
    void update_BookExistsInRepo_BookDataInRepoShouldBeChanged() {
        var book = new Book("Тестовая книга", "Неизвестный жанр", 200);
        var bookId = booksDao.save(book);
        book.setId(bookId);

        var bookInDb = booksDao.get(bookId);
        assertThat(bookInDb).isEqualTo(book);

        var newTitle = "Test book";
        var newGenre = "Unknown genre";
        var newNumberOfPages = 300;

        book.setTitle(newTitle);
        book.setGenre(newGenre);
        book.setNumberOfPages(newNumberOfPages);

        booksDao.update(book);
        bookInDb = booksDao.get(bookId);

        assertThat(bookInDb.getTitle()).isEqualTo(newTitle);
        assertThat(bookInDb.getGenre()).isEqualTo(newGenre);
        assertThat(bookInDb.getNumberOfPages()).isEqualTo(newNumberOfPages);
    }

    @Test
    void delete_BookExistsInRepo_BookShouldNotBeFoundByGetAfterDelete() {
        var book = new Book("Книга для удаления", "Жанр неизвестен", 200);
        var bookId = booksDao.save(book);

        assertFalse(booksDao.get(bookId).isDeleted());
        booksDao.delete(bookId);
        assertThatThrownBy(() -> booksDao.get(bookId))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void addBooks_FileWithCorrectFormat_ShouldSuccessfullySaveAllBooksInRepo() {
        var booksCount = booksDao.getAll().size();

        var booksToLoad = booksLoader.loadBooksFromFile(CORRECT_BOOKS_FILE_NAME);
        booksDao.addBooks(booksToLoad);

        assertThat(booksToLoad.size()).isGreaterThan(0);
        assertThat(booksDao.getAll().size()).isEqualTo(booksCount + booksToLoad.size());
    }

    @Test
    void getForAuthor_AuthorWithTwoBooks_ShouldReturnListOfTwoBooks() {
        var bookAuthors = booksDao.getForAuthor(SAM_NEWMAN.getId());

        assertThat(bookAuthors).hasOnlyElementsOfType(Book.class);
        assertThat(bookAuthors).hasSize(2);
        assertThat(bookAuthors).containsAll(List.of(BUILDING_MICROSERVICES, MONOLITH_TO_MICROSERVICES));
    }

    @Test
    void getForAuthor_AuthorWithOneBook_ShouldReturnListOfOneBook() {
        var bookAuthors = booksDao.getForAuthor(CRAIG_WALLS.getId());

        assertThat(bookAuthors).hasOnlyElementsOfType(Book.class);
        assertThat(bookAuthors).hasSize(1);
        assertThat(bookAuthors).containsAll(List.of(SPRING_IN_ACTION));
    }

    @Test
    void getForAuthor_AuthorWithoutBooks_ShouldReturnEmpty() {
        var bookAuthors = booksDao.getForAuthor(9999);

        assertThat(bookAuthors).isEmpty();
    }
}