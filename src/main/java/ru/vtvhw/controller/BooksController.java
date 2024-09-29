package ru.vtvhw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vtvhw.service.BooksService;

import java.util.stream.Collectors;

import static java.lang.String.format;

@RestController
public class BooksController {
    private BooksService booksService;

    @Autowired
    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @RequestMapping("/books")
    public String books() {
        var src = "<h1>List of Books:</h1></br></br>";
        src += booksService.getAllBooks()
                .stream()
                .map(book -> format("[%d] %s (%s, %d стр.) [<a href=\"/bookDetails?id=%d\">details</a>]",
                        book.getId(), book.getTitle(), book.getAuthor(), book.getNumberOfPages(), book.getId()))
                .collect(Collectors.joining("</br>"));
        src += "<br><br><a href=\"/\"><< Back</a>";
        return src;
    }
}