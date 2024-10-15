package ru.vtvhw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.vtvhw.config.BooksAppConfig;
import ru.vtvhw.exceptions.BooksLoadFileFormatException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Unit tests for {@link BooksLoader}.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BooksAppConfig.class)
public record BooksLoaderTest(@Autowired BooksLoader booksLoader) {
    private static final String CORRECT_BOOKS_FILE_NAME = "csv/OneThousandTestBooks.csv";
    private static final String WRONG_FILE_NAME = "csv/WrongFileName.csv";
    private static final String EXTRA_COLUMN_FILE_NAME = "csv/MoreColumnsThanNecessaryForBooksLoad.csv";
    private static final String WRONG_FIELD_FORMAT_FILE_NAME = "csv/WrongFieldFormatForBooksLoad.csv";
    private static final String LESS_COLUMNS_FILE_NAME = "csv/NotEnoughColumnsForBooksLoad.csv";

    @Test
    void testSuccessLoadBooks() {
        var booksFromCsvFile = booksLoader.loadBooksFromFile(CORRECT_BOOKS_FILE_NAME);
        assertThat(booksFromCsvFile.size()).isEqualTo(1000);
    }

    @Test
    void testFailedLoadBooks_WrongFileName() {
        var booksFromCsvFile = booksLoader.loadBooksFromFile(WRONG_FILE_NAME);
        assertThat(booksFromCsvFile.size()).isEqualTo(0);
    }

    @Test
    void testFailedLoadBooks_WrongAmountOfColumns() {
        assertThatThrownBy(() -> booksLoader.loadBooksFromFile(EXTRA_COLUMN_FILE_NAME))
                .isInstanceOf(BooksLoadFileFormatException.class);
    }

    @Test
    void testFailedLoadBooks_WrongFieldFormat() {
        assertThatThrownBy(() -> booksLoader.loadBooksFromFile(WRONG_FIELD_FORMAT_FILE_NAME))
                .isInstanceOf(BooksLoadFileFormatException.class);
    }

    @Test
    void testFailedLoadBooks_NotEnoughColumns() {
        assertThatThrownBy(() -> booksLoader.loadBooksFromFile(LESS_COLUMNS_FILE_NAME))
                .isInstanceOf(BooksLoadFileFormatException.class);
    }
}