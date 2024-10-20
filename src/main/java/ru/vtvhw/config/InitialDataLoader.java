package ru.vtvhw.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.vtvhw.repo.AuthorsRepository;
import ru.vtvhw.repo.BooksRepository;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
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
    private final BooksRepository booksRepository;

    @Autowired
    private final AuthorsRepository authorsRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadInitialDataAfterStartup() {
        booksRepository.saveAll(List.of(SPRING_IN_ACTION, DESIGN_PATTERNS, THE_PHILOSOPHY_OF_JAVA, BUILDING_MICROSERVICES));
        authorsRepository.saveAll(List.of(CRAIG_WALLS, ERIC_FREEMAN, ELIZABETH_ROBSON, BRUCE_ECKEL, SAM_NEWMAN));
        var deletedBook = booksRepository.save(DELETED_BOOK);
        booksRepository.save(MONOLITH_TO_MICROSERVICES);
        var deletedAuthor = authorsRepository.save(DELETED_AUTHOR);

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

        authorsRepository.saveAll(List.of(CRAIG_WALLS, ERIC_FREEMAN, ELIZABETH_ROBSON,
                BRUCE_ECKEL, SAM_NEWMAN, DELETED_AUTHOR));

        authorsRepository.delete(deletedAuthor);
        booksRepository.delete(deletedBook);

        log.info("Books loaded: {}", booksRepository.findAll());
        log.info("Authors loaded: {}", authorsRepository.findAll());
    }
}