package ru.vtvhw.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vtvhw.model.Book;
import ru.vtvhw.service.BooksService;

@Controller
@RequestMapping("/books")
@Slf4j
public class BooksController {

    private final BooksService booksService;

    @Autowired
    public BooksController(BooksService booksService) {
        log.debug("BooksController initialized");
        this.booksService = booksService;
    }

    @GetMapping("/")
    public String books(Model model) {
        log.info("[GET] Show all books");
        model.addAttribute("books", booksService.findAll());
        return "books-list-form";
    }

    @GetMapping("/view")
    public String bookDetails(@RequestParam("id") long bookId, Model model) {
        log.info("[GET] View book with ID = {}", bookId);
        var book = booksService.findById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("authors", book.getAuthors());
        return "books-view-form";
    }

    @GetMapping("/create-form")
    public String createForm() {
        log.info("[GET] Open form to create new book");
        return "books-create-form";
    }

    @PostMapping("/create")
    public String create(Book book) {
        log.info("[POST] Create new book: {}", book);
        booksService.save(book);
        return "redirect:/books/";
    }

    @GetMapping("/edit-form/{id}")
    public String editForm(@PathVariable("id") String bookId, Model model) {
        log.info("[GET] Edit book with ID = {}", bookId);
        var book = booksService.findById(Long.parseLong(bookId));
        model.addAttribute("book", book);
        model.addAttribute("authors", book.getAuthors());
        return "books-edit-form";
    }

    @PutMapping("/update")
    public String update(Book book) {
        log.info("[PUT] Update book: {}", book);
        booksService.save(book);
        return "redirect:/books/";
    }

    @PutMapping("/delete/{removedBookId}")
    public String delete(@PathVariable("removedBookId") long bookId) {
        log.info("[PUT] Delete book with ID = {}", bookId);
        booksService.deleteById(bookId);
        return "redirect:/books/";
    }

    @PutMapping("/delete/{bookId}/author/{removedAuthorId}")
    public String deleteAuthorFromBook(@PathVariable("bookId") long bookId, @PathVariable("removedAuthorId") long authorId) {
        log.info("[PUT] Delete author with ID = {} for book with ID = {}", authorId, bookId);
        var book = booksService.findById(bookId);
        var authorToRemove = book.getAuthors().stream().filter(author -> author.getId() == authorId).findFirst();
        authorToRemove.ifPresent(it -> booksService.removeAuthor(book, it));
        return "redirect:/books/edit-form/" + book.getId();
    }
}