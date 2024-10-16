package ru.vtvhw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vtvhw.model.Book;
import ru.vtvhw.service.BooksService;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;

    @Autowired
    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/")
    public String books(Model model) {
        model.addAttribute("books", booksService.findAll());
        return "books-list-form";
    }

    @GetMapping("/view")
    public String bookDetails(@RequestParam("id") long bookId, Model model) {
        var book = booksService.findById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("authors", book.getAuthors());
        return "books-view-form";
    }

    @GetMapping("/create-form")
    public String createForm() {
        return "books-create-form";
    }

    @PostMapping("/create")
    public String create(Book book) {
        booksService.save(book);
        return "redirect:/books/";
    }

    @GetMapping("/edit-form/{id}")
    public String editForm(@PathVariable("id") String bookId, Model model) {
        var book = booksService.findById(Long.parseLong(bookId));
        model.addAttribute("book", book);
        model.addAttribute("authors", book.getAuthors());
        return "books-edit-form";
    }

    @PutMapping("/update")
    public String update(Book book) {
        booksService.save(book);
        return "redirect:/books/";
    }

    @PutMapping("/delete/{removedAuthorId}")
    public String delete(@PathVariable("removedAuthorId") long bookId) {
        booksService.deleteById(bookId);
        return "redirect:/books/";
    }

    @PutMapping("/delete/{bookId}/author/{removedAuthorId}")
    public String deleteAuthorFromBook(@PathVariable("bookId") long bookId, @PathVariable("removedAuthorId") long authorId) {
        var book = booksService.findById(bookId);
        var authorToRemove = book.getAuthors().stream().filter(author -> author.getId() == authorId).findFirst();
        authorToRemove.ifPresent(it -> booksService.removeAuthor(book, it));
        return "redirect:/books/edit-form/" + book.getId();
    }
}