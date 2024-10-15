package ru.vtvhw.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ru.vtvhw.model.Book;
import ru.vtvhw.exceptions.BooksLoadFileFormatException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class BooksLoader {
    @Autowired
    ResourceLoader resourceLoader;

    private static final Logger log = LoggerFactory.getLogger(BooksLoader.class);

    public List<Book> loadBooksFromFile(String fileName) {
        var books = new ArrayList<Book>();
        var resource = resourceLoader.getResource(fileName);
        AtomicInteger rowNb = new AtomicInteger();

        try(Stream<String> rows = Files.lines( resource.getFile().toPath(), Charset.forName("windows-1251") )) {
            rows.forEach(row -> {
                if (rowNb.getAndIncrement() > 0) {
                    var bookRowParts = row.split(";");

                    if (bookRowParts.length != 4) {
                        throw new BooksLoadFileFormatException();
                    }

                    var title = bookRowParts[0];
                    var author = bookRowParts[1];
                    var genre = bookRowParts[2];
                    var pages = Integer.parseInt(bookRowParts[3]);

                    books.add(new Book(0, title, genre, pages));
                }
            });
        } catch (IOException e) {
            log.error("No such file: {}", fileName);
            return books;
        } catch (NumberFormatException e) {
            throw new BooksLoadFileFormatException(e);
        }
        return books;
    }
}