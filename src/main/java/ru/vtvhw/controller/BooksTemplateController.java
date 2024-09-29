package ru.vtvhw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vtvhw.service.BooksService;

@Controller
public class BooksTemplateController {

    private BooksService booksService;

    @Autowired
    public BooksTemplateController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/bookDetails")
    public String bookDetails(@RequestParam("id") long bookId, Model model) {
        var book = booksService.getBookById(bookId);

        model.addAttribute("bookId", book.getId());
        model.addAttribute("bookTitle", book.getTitle());
        model.addAttribute("bookAuthor", book.getAuthor());
        model.addAttribute("bookNumberOfPages", book.getNumberOfPages());

        return "bookDetails";
    }
}