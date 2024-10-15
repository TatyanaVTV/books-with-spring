package ru.vtvhw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.vtvhw.config.BooksAppConfig;
import ru.vtvhw.exceptions.AuthorNotFoundException;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link AuthorsDao}.
 * Аннотация @Sql подтягивает SQL-скрипты books-db.sql, authors-db.sql и authors-to-books-db.sql,
 * которыё будут применен к базе перед выполнением тестов.
 * books-db.sql создает таблицу books с полями (book_id, title,  genre, pages, deleted) и вставляет в неё 6 записей.
 * authors-db.sql создает таблицу authors с полями (author_id, author_name, deleted) и вставляет в неё 5 записей.
 * authors-to-books-db.sql создаёт таблицу author_books с полями (author_id, book_id) и вставляет в неё 6 записей.
 * Тесты проверяют корректность реализации AuthorsDao.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BooksAppConfig.class)
@Sql("classpath:/db/books-db.sql")
@Sql("classpath:/db/authors-db.sql")
@Sql("classpath:/db/authors-to-books-db.sql")
public record AuthorsDaoTest(@Autowired DataSource dataSource, @Autowired AuthorsDao authorsDao) {

    private static final Author CRAIG_WALLS = new Author(1L, "Крейг Уоллс", false, new HashSet<>());
    private static final Author ERIC_FREEMAN = new Author(2L, "Эрик Фримен", false, new HashSet<>());
    private static final Author ELIZABETH_ROBSON = new Author(3L, "Элизабет Робсон", false, new HashSet<>());
    private static final Author BRUCE_ECKEL = new Author(4L, "Брюс Эккель", false, new HashSet<>());
    private static final Author SAM_NEWMAN = new Author(5L, "Сэм Ньюмен", false, new HashSet<>());
    private static final Author DELETED_AUTHOR = new Author(6L, "Автор неизвестен", true, new HashSet<>());

    private static final Book SPRING_IN_ACTION =
            new Book(1L, "Spring в действии", "Учебная литература", 543, false);

    private static final Book DESIGN_PATTERNS =
            new Book(2L, "Head First. Паттерны проектирования", "Учебная литература", 640, false);

    /**
     * There are four NOT deleted books inserted in the database in contact.sql.
     */
    private static final List<Author> PERSISTED_AUTHORS =
            List.of(CRAIG_WALLS, ERIC_FREEMAN, BRUCE_ECKEL, SAM_NEWMAN);

    @Test
    void save_AuthorDoesNotExistInRepo_AuthorInRepoShouldBeEqualToSaved() {
        var author = new Author("Неизвестный автор");
        var authorId = authorsDao.save(author);
        author.setId(authorId);

        var authorInDb = authorsDao.get(authorId);

        assertThat(authorInDb).isEqualTo(author);
    }

    @Test
    void get_AuthorExistsInRepo_ShouldBeFoundAndEqualToExpected() {
        var author = authorsDao.get(CRAIG_WALLS.getId());

        assertThat(author).isEqualTo(CRAIG_WALLS);
    }

    @Test
    void get_AuthorDoesNotExistInRepo_ShouldThrowAuthorNotFoundException() {
        assertThatThrownBy(() -> authorsDao.get(99999))
                .isInstanceOf(AuthorNotFoundException.class);
    }

    @Test
    void getAll_PreLoadedAuthors_ShouldReturnListOfAllNotDeletedAuthors() {
        var author = authorsDao.getAll();

        assertThat(author).containsAll(PERSISTED_AUTHORS);
        assertThat(author).doesNotContain(DELETED_AUTHOR);
    }

    @Test
    void find_AuthorExistsInRepo_ShouldReturnNotEmptyOptionalOfExpectedAuthor() {
        var existingAuthor = authorsDao.find(SAM_NEWMAN.getId());

        assertThat(existingAuthor).isEqualTo(Optional.of(SAM_NEWMAN));
    }

    @Test
    void find_AuthorDoesNotExistInRepo_ShouldReturnEmptyOptional() {
        var notExistingAuthor = authorsDao.find(99999);
        assertThat(notExistingAuthor).isEqualTo(Optional.empty());
    }

    @Test
    void update_AuthorExistsInRepo_AuthorDataInRepoShouldBeChanged() {
        var author = new Author("Неизвестный автор");
        var authorId = authorsDao.save(author);
        author.setId(authorId);

        var authorInDb = authorsDao.get(authorId);
        assertThat(authorInDb).isEqualTo(author);

        var newName = "Test author";
        author.setName(newName);

        authorsDao.update(author);
        authorInDb = authorsDao.get(authorId);

        assertThat(authorInDb.getName()).isEqualTo(newName);
        authorsDao.delete(authorInDb);
    }

    @Test
    void delete_AuthorExistsInRepo_AuthorShouldNotBeFoundByGetAfterDelete() {
        var author = new Author("Автор неизвестен 2");
        var authorId = authorsDao.save(author);

        var authorFromDb = authorsDao.get(authorId);

        assertThat(authorFromDb).isNotNull();
        assertFalse(authorFromDb.isDeleted());

        authorsDao.delete(authorId);
        assertThatThrownBy(() -> authorsDao.get(authorId))
                .isInstanceOf(AuthorNotFoundException.class);
    }

    @Test
    void getForAuthor_AuthorWithTwoBooks_ShouldReturnListOfTwoBooks() {
        var authorBooks = authorsDao.getForBook(DESIGN_PATTERNS.getId());

        assertThat(authorBooks).hasOnlyElementsOfType(Author.class);
        assertThat(authorBooks).hasSize(2);
        assertThat(authorBooks).containsAll(List.of(ERIC_FREEMAN, ELIZABETH_ROBSON));
    }

    @Test
    void getForAuthor_AuthorWithOneBook_ShouldReturnListOfOneBook() {
        var authorBooks = authorsDao.getForBook(SPRING_IN_ACTION.getId());

        assertThat(authorBooks).hasOnlyElementsOfType(Author.class);
        assertThat(authorBooks).hasSize(1);
        assertThat(authorBooks).containsAll(List.of(CRAIG_WALLS));
    }

    @Test
    void getForAuthor_AuthorWithoutBooks_ShouldReturnEmpty() {
        var authorBooks = authorsDao.getForBook(9999);

        assertThat(authorBooks).isEmpty();
    }
}