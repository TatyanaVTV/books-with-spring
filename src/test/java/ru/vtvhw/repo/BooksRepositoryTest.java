package ru.vtvhw.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.vtvhw.config.BooksAppConfig;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;
import ru.vtvhw.service.BooksLoader;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link BooksRepository}.
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
public record BooksRepositoryTest(@Autowired DataSource dataSource,
                                  @Autowired BooksRepository booksRepository,
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
        var savedBook = booksRepository.save(book);
        book.setId(savedBook.getId());

        assertThat(savedBook).isEqualTo(book);
    }

    @Test
    void findAll_PreLoadedBooks_ShouldReturnListOfAllNotDeletedBooks() {
        var books = booksRepository.findAll();

        assertThat(books).containsAll(PERSISTED_BOOKS);
        assertThat(books).doesNotContain(DELETED_BOOK);
    }

//    @Test
//    void get_BookExistsInRepo_ShouldBeFoundAndEqualToExpected() {
//        var book = booksRepository.get(DESIGN_PATTERNS.getId());
//
//        assertThat(book).isEqualTo(DESIGN_PATTERNS);
//    }

//    @Test
//    void get_BookDoesNotExistInRepo_ShouldThrowBookNotFoundException() {
//        assertThatThrownBy(() -> booksRepository.get(99999))
//                .isInstanceOf(BookNotFoundException.class);
//    }

    @Test
    void findById_BookExistsInRepo_ShouldReturnNotEmptyOptionalOfExpectedBook() {
        var existingBook = booksRepository.findById(BUILDING_MICROSERVICES.getId());
        assertThat(existingBook).isEqualTo(Optional.of(BUILDING_MICROSERVICES));
    }

    @Test
    void findById_BookDoesNotExistInRepo_ShouldReturnEmptyOptional() {
        var notExistingBook = booksRepository.findById(Long.MAX_VALUE);
        assertThat(notExistingBook).isEqualTo(Optional.empty());
    }

    @Test
    void save_BookExistsInRepo_BookDataInRepoShouldBeChanged() {
        var book = new Book("Тестовая книга", "Неизвестный жанр", 200);
        var savedBook = booksRepository.save(book);
        book.setId(savedBook.getId());
        assertThat(savedBook).isEqualTo(book);

        var newTitle = "Test book";
        var newGenre = "Unknown genre";
        var newNumberOfPages = 300;

        book.setTitle(newTitle);
        book.setGenre(newGenre);
        book.setNumberOfPages(newNumberOfPages);

        savedBook = booksRepository.save(book);
//        bookInDb = booksRepository.get(bookId);

        assertThat(savedBook.getTitle()).isEqualTo(newTitle);
        assertThat(savedBook.getGenre()).isEqualTo(newGenre);
        assertThat(savedBook.getNumberOfPages()).isEqualTo(newNumberOfPages);
        booksRepository.delete(savedBook);
    }

    @Test
    void delete_BookExistsInRepo_BookShouldNotBeFoundByGetAfterDelete() {
        var book = new Book("Книга для удаления", "Жанр неизвестен", 200);
        var savedBook = booksRepository.save(book);

        assertThat(savedBook).isNotEqualTo(Optional.empty());
        assertFalse(savedBook.isDeleted());

        booksRepository.delete(savedBook);
        assertThat(booksRepository.findById(savedBook.getId())).isEqualTo(Optional.empty());
    }

    @Test
    void addBooks_FileWithCorrectFormat_ShouldSuccessfullySaveAllBooksInRepo() {
        var booksCount = booksRepository.findAll().size();

        var booksToLoad = booksLoader.loadBooksFromFile(CORRECT_BOOKS_FILE_NAME);
        booksRepository.saveAll(booksToLoad);

        assertThat(booksToLoad.size()).isGreaterThan(0);
        assertThat(booksRepository.findAll().size()).isEqualTo(booksCount + booksToLoad.size());
    }

    @Test
    void getForAuthor_AuthorWithTwoBooks_ShouldReturnListOfTwoBooks() {
        var bookAuthors = booksRepository.getForAuthor(SAM_NEWMAN.getId());

        assertThat(bookAuthors).hasOnlyElementsOfType(Book.class);
        assertThat(bookAuthors).hasSize(2);
        assertThat(bookAuthors).containsAll(List.of(BUILDING_MICROSERVICES, MONOLITH_TO_MICROSERVICES));
    }

    @Test
    void getForAuthor_AuthorWithOneBook_ShouldReturnListOfOneBook() {
        var bookAuthors = booksRepository.getForAuthor(CRAIG_WALLS.getId());

        assertThat(bookAuthors).hasOnlyElementsOfType(Book.class);
        assertThat(bookAuthors).hasSize(1);
        assertThat(bookAuthors).containsAll(List.of(SPRING_IN_ACTION));
    }

    @Test
    void getForAuthor_AuthorWithoutBooks_ShouldReturnEmpty() {
        var bookAuthors = booksRepository.getForAuthor(9999);

        assertThat(bookAuthors).isEmpty();
    }
}