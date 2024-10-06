package ru.vtvhw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vtvhw.model.Book;
import ru.vtvhw.service.BooksService;

@Controller
public class BooksController {

    private final BooksService booksService;

    @Autowired
    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/books")
    public String books(Model model) {
        model.addAttribute("books", booksService.getAllBooks());
        return "booksList";
    }

    @GetMapping("/bookDetails")
    public String bookDetails(@RequestParam("id") long bookId, Model model) {
        var book = booksService.getBookById(bookId);
        model.addAttribute("book", book);
        return "bookDetails";
    }

    @GetMapping("/books/create-form")
    public String createForm() {
        return "create-book-form";
    }

    @PostMapping("/books/create")
    public String create(Book book) {
        booksService.createBook(book);
        return "redirect:/books";
    }

    @GetMapping("/books/edit-form/{id}")
    public String editForm(@PathVariable("id") String bookId, Model model) {
        var book = booksService.getBookById(Long.parseLong(bookId));
        model.addAttribute("book", book);
        return "edit-book-form";
    }

    @PutMapping("/books/update")
    public String update(Book book) {
        booksService.updateBook(book.getId(), book);
        return "redirect:/books";
    }


    @PutMapping("/books/delete/{id}")
    public String delete(Book book) {
        booksService.deleteBookById(book.getId());
        return "redirect:/books";
    }
}