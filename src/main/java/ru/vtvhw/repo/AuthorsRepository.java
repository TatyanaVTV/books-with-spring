package ru.vtvhw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.vtvhw.model.Author;

import java.util.List;

public interface AuthorsRepository extends JpaRepository<Author, Long> {

    @Transactional
    @Query("SELECT a FROM Author a JOIN a.books b WHERE b.id = :bookId")
    List<Author> getForBook(@Param("bookId") long bookId);
}