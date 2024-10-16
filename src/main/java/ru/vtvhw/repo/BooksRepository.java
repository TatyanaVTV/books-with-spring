package ru.vtvhw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.vtvhw.model.Book;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {
    @Transactional
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    List<Book> getForAuthor(@Param("authorId") long authorId);
}