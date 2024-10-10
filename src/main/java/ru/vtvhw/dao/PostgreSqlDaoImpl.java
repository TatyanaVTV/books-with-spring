package ru.vtvhw.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.vtvhw.model.Book;
import ru.vtvhw.model.BookNotFoundException;

import java.util.List;
import java.util.Optional;

public class PostgreSqlDaoImpl implements BooksDao {

    private static final String GET_ALL_BOOKS_SQL = """
                SELECT id, title, author, genre, pages, deleted
                FROM books
                WHERE deleted = false
            """.trim();

    private static final String GET_BOOK_BY_ID_SQL = """
                SELECT id, title, author, genre, pages, deleted
                FROM books
                WHERE id = :bookId
            """.trim();

    public static final String INSERT_INTO_BOOKS_SQL = """
                INSERT INTO books(title, author, genre, pages, deleted)
                VALUES (:title, :author, :genre, :pages, :deleted)
            """.trim();

    private static final String DELETE_BOOK_BY_ID_SQL = "UPDATE books SET deleted = :deleted WHERE id = :bookId";

    private static final String UPDATE_BOOK_BY_ID_SQL = """
                UPDATE books
                SET title = :title, author = :author, genre = :genre, pages = :pages, deleted = :deleted
                WHERE id = :bookId
            """.trim();

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private static final RowMapper<Book> BOOK_ROW_MAPPER =
            (rs, i) -> new Book(
                    rs.getLong("ID"),
                    rs.getString("TITLE"),
                    rs.getString("AUTHOR"),
                    rs.getString("GENRE"),
                    rs.getInt("PAGES"),
                    rs.getBoolean("DELETED"));

    @Override
    public List<Book> getAllBooks() {
        return namedJdbcTemplate.query(
                GET_ALL_BOOKS_SQL,
                BOOK_ROW_MAPPER
        );
    }

    @Override
    public Optional<Book> findBookById(long bookId) {
        var result = namedJdbcTemplate.query(
                GET_BOOK_BY_ID_SQL,
                new MapSqlParameterSource("bookId", bookId),
                BOOK_ROW_MAPPER
        );
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Book getBookById(long bookId) throws BookNotFoundException {
        try {
            return namedJdbcTemplate.queryForObject(
                    GET_BOOK_BY_ID_SQL,
                    new MapSqlParameterSource("bookId", bookId),
                    BOOK_ROW_MAPPER
            );
        } catch (EmptyResultDataAccessException e) {
            throw new BookNotFoundException(bookId);
        }
    }


    @Override
    public long addBook(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
            INSERT_INTO_BOOKS_SQL,
            new MapSqlParameterSource("title", book.getTitle())
                    .addValue("author", book.getAuthor())
                    .addValue("genre", book.getGenre())
                    .addValue("pages", book.getNumberOfPages())
                    .addValue("deleted", book.isDeleted())
                , keyHolder
                , new String[] { "id" }
        );

        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteBookById(long bookId) {
        namedJdbcTemplate.update(
                DELETE_BOOK_BY_ID_SQL,
                new MapSqlParameterSource("deleted", true).addValue("bookId", bookId)
        );
    }

    @Override
    public void updateBookById(long bookId, Book book) {
        namedJdbcTemplate.update(
                UPDATE_BOOK_BY_ID_SQL,
                new MapSqlParameterSource("title", book.getTitle())
                        .addValue("author", book.getAuthor())
                        .addValue("genre", book.getGenre())
                        .addValue("pages", book.getNumberOfPages())
                        .addValue("deleted", book.isDeleted())
                        .addValue("bookId", bookId)
        );
    }
}