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

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link AuthorsRepository}.
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
public record AuthorsRepositoryTest(@Autowired DataSource dataSource, @Autowired AuthorsRepository authorsRepository) {

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
        var savedAuthor = authorsRepository.save(author);
        author.setId(savedAuthor.getId());

        assertThat(savedAuthor).isEqualTo(author);
    }

    @Test
    void findAll_PreLoadedAuthors_ShouldReturnListOfAllNotDeletedAuthors() {
        var authors = authorsRepository.findAll();

        assertThat(authors).containsAll(PERSISTED_AUTHORS);
        assertThat(authors).doesNotContain(DELETED_AUTHOR);
    }

//    @Test
//    void findById_AuthorExistsInRepo_ShouldBeFoundAndEqualToExpected() {
//        var author = authorsDao.findById(CRAIG_WALLS.getId());
//
//        assertTrue(author.isPresent());
//        assertThat(author.get()).isEqualTo(CRAIG_WALLS);
//    }

//    @Test
//    void get_AuthorDoesNotExistInRepo_ShouldThrowAuthorNotFoundException() {
//        var notExistingAuthor = authorsDao.findById(Long.MAX_VALUE);
//        assertThat(notExistingAuthor).isNotPresent();
//    }

    @Test
    void findById_AuthorExistsInRepo_ShouldReturnNotEmptyOptionalOfExpectedAuthor() {
        var existingAuthor = authorsRepository.findById(SAM_NEWMAN.getId());
        assertThat(existingAuthor).isEqualTo(Optional.of(SAM_NEWMAN));
    }

    @Test
    void findById_AuthorDoesNotExistInRepo_ShouldReturnEmptyOptional() {
        var notExistingAuthor = authorsRepository.findById(Long.MAX_VALUE);
        assertThat(notExistingAuthor).isEqualTo(Optional.empty());
    }

    @Test
    void save_AuthorExistsInRepo_AuthorDataInRepoShouldBeChanged() {
        var author = new Author("Неизвестный автор");
        var savedAuthor = authorsRepository.save(author);
        author.setId(savedAuthor.getId());
        assertThat(savedAuthor).isEqualTo(author);

        var newName = "Test author";
        author.setName(newName);

        savedAuthor = authorsRepository.save(author);
//        savedAuthor = authorsDao.findById(savedAuthor.getId()).get();

        assertThat(savedAuthor.getName()).isEqualTo(newName);
        authorsRepository.delete(savedAuthor);
    }

    @Test
    void delete_AuthorExistsInRepo_AuthorShouldNotBeFoundByGetAfterDelete() {
        var author = new Author("Автор неизвестен 2");
        var savedAuthor = authorsRepository.save(author);

        assertThat(savedAuthor).isNotEqualTo(Optional.empty());
        assertFalse(savedAuthor.isDeleted());

        authorsRepository.delete(savedAuthor);
        assertThat(authorsRepository.findById(savedAuthor.getId())).isEqualTo(Optional.empty());
//        assertThatThrownBy(() -> authorsDao.findById(savedAuthor))
//                .isInstanceOf(AuthorNotFoundException.class);
    }

    @Test
    void getForAuthor_AuthorWithTwoBooks_ShouldReturnListOfTwoBooks() {
        var authorBooks = authorsRepository.getForBook(DESIGN_PATTERNS.getId());

        assertThat(authorBooks).hasOnlyElementsOfType(Author.class);
        assertThat(authorBooks).hasSize(2);
        assertThat(authorBooks).containsAll(List.of(ERIC_FREEMAN, ELIZABETH_ROBSON));
    }

    @Test
    void getForAuthor_AuthorWithOneBook_ShouldReturnListOfOneBook() {
        var authorBooks = authorsRepository.getForBook(SPRING_IN_ACTION.getId());

        assertThat(authorBooks).hasOnlyElementsOfType(Author.class);
        assertThat(authorBooks).hasSize(1);
        assertThat(authorBooks).containsAll(List.of(CRAIG_WALLS));
    }

    @Test
    void getForAuthor_AuthorWithoutBooks_ShouldReturnEmpty() {
        var authorBooks = authorsRepository.getForBook(9999);

        assertThat(authorBooks).isEmpty();
    }
}