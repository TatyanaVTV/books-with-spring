package ru.vtvhw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vtvhw.dao.AuthorsDao;
import ru.vtvhw.dao.BooksDao;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;

//@Component
@RequiredArgsConstructor
public class InitialDataLoader {

    public static final Author CRAIG_WALLS = new Author(1L, "Крейг Уоллс");
    public static final Author ERIC_FREEMAN = new Author(2L, "Эрик Фримен");
    public static final Author ELIZABETH_ROBSON = new Author(3L, "Элизабет Робсон");
    public static final Author BRUCE_ECKEL = new Author(4L, "Брюс Эккель");
    public static final Author SAM_NEWMAN = new Author(5L, "Сэм Ньюмен");
    public static final Author DELETED_AUTHOR = new Author(6L, "Автор неизвестен");

    public static final Book SPRING_IN_ACTION = new Book(1L, "Spring в действии", "Учебная литература", 543);
    public static final Book DESIGN_PATTERNS = new Book(2L, "Head First. Паттерны проектирования", "Учебная литература", 640);
    public static final Book THE_PHILOSOPHY_OF_JAVA = new Book(3L, "Философия Java", "Учебная литература", 1168);
    public static final Book BUILDING_MICROSERVICES = new Book(4L, "Создание микросервисов", "Учебная литература", 624);
    public static final Book DELETED_BOOK = new Book(5L, "Удаленная книга", "Жанр неизвестен", 100);
    private static final Book MONOLITH_TO_MICROSERVICES =
            new Book(6L, "От монолита к микросервисам", "Учебная литература", 272);

    @Autowired
    private final BooksDao booksDao;

    @Autowired
    private final AuthorsDao authorsDao;

    //    @EventListener(ApplicationReadyEvent.class)
    public void loadInitialDataAfterStartup() {
        loadBooks();
        loadAuthors();

        SPRING_IN_ACTION.getAuthors().add(CRAIG_WALLS);
        CRAIG_WALLS.getBooks().add(SPRING_IN_ACTION);

        DESIGN_PATTERNS.getAuthors().add(ERIC_FREEMAN);
        DESIGN_PATTERNS.getAuthors().add(ELIZABETH_ROBSON);
        ERIC_FREEMAN.getBooks().add(DESIGN_PATTERNS);
        ELIZABETH_ROBSON.getBooks().add(DESIGN_PATTERNS);

        THE_PHILOSOPHY_OF_JAVA.getAuthors().add(BRUCE_ECKEL);
        BRUCE_ECKEL.getBooks().add(THE_PHILOSOPHY_OF_JAVA);

        BUILDING_MICROSERVICES.getAuthors().add(SAM_NEWMAN);
        MONOLITH_TO_MICROSERVICES.getAuthors().add(SAM_NEWMAN);
        SAM_NEWMAN.getBooks().add(BUILDING_MICROSERVICES);
        SAM_NEWMAN.getBooks().add(MONOLITH_TO_MICROSERVICES);

        DELETED_BOOK.getAuthors().add(DELETED_AUTHOR);
        DELETED_AUTHOR.getBooks().add(DELETED_BOOK);

        authorsDao.update(CRAIG_WALLS);
        authorsDao.update(ERIC_FREEMAN);
        authorsDao.update(ELIZABETH_ROBSON);
        authorsDao.update(BRUCE_ECKEL);
        authorsDao.update(SAM_NEWMAN);
        authorsDao.update(DELETED_AUTHOR);

        authorsDao.delete(DELETED_AUTHOR);
        booksDao.delete(DELETED_BOOK);

        booksDao.getAll().forEach(System.out::println);
        authorsDao.getAll().forEach(System.out::println);
    }

    private void loadAuthors() {
        authorsDao.save(CRAIG_WALLS);
        authorsDao.save(ERIC_FREEMAN);
        authorsDao.save(ELIZABETH_ROBSON);
        authorsDao.save(BRUCE_ECKEL);
        authorsDao.save(SAM_NEWMAN);
        authorsDao.save(DELETED_AUTHOR);
    }

    private void loadBooks() {
        booksDao.save(SPRING_IN_ACTION);
        booksDao.save(DESIGN_PATTERNS);
        booksDao.save(THE_PHILOSOPHY_OF_JAVA);
        booksDao.save(BUILDING_MICROSERVICES);
        booksDao.save(DELETED_BOOK);
        booksDao.save(MONOLITH_TO_MICROSERVICES);
    }
}