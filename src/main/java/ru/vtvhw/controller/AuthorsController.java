package ru.vtvhw.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vtvhw.model.Author;
import ru.vtvhw.service.AuthorsService;

@Controller
@RequestMapping("/authors")
@Slf4j
public class AuthorsController {

    private final AuthorsService authorsService;

    @Autowired
    public AuthorsController(AuthorsService authorsService) {
        log.debug("AuthorsController initialized");
        this.authorsService = authorsService;
    }

    @GetMapping("/")
    public String authors(Model model) {
        log.info("[GET] Show all authors");
        model.addAttribute("authors", authorsService.findAll());
        return "authors-list-form";
    }

    @GetMapping("/view")
    public String viewAuthor(@RequestParam("id") long authorId, Model model) {
        log.info("[GET] View author with ID = {}", authorId);
        var author = authorsService.findById(authorId);
        model.addAttribute("author", author);
        model.addAttribute("books", author.getBooks());
        return "authors-view-form";
    }

    @GetMapping("/create-form")
    public String createForm() {
        log.info("[GET] Open form to create new author");
        return "authors-create-form";
    }

    @PostMapping("/create")
    public String createAuthor(Author author) {
        log.info("[POST] Create new author: {}", author);
        authorsService.save(author);
        return "redirect:/authors/";
    }

    @GetMapping("/edit-form/{authorId}")
    public String editForm(@PathVariable("authorId") String authorId, Model model) {
        var author = authorsService.findById(Long.parseLong(authorId));
        model.addAttribute("author", author);
        model.addAttribute("books", author.getBooks());
        return "authors-edit-form";
    }

    @PutMapping("/update")
    public String updateAuthor(Author author) {
        log.info("[PUT] Update author: {}", author);
        authorsService.save(author);
        return "redirect:/authors/";
    }

    @PutMapping("/delete/{authorId}")
    public String deleteAuthor(@PathVariable("authorId") long authorId) {
        log.info("[PUT] Delete author with ID = {}", authorId);
        authorsService.deleteById(authorId);
        return "redirect:/authors/";
    }

    @PutMapping("/delete/{authorId}/book/{removedBookId}")
    public String deleteBookFromAuthor(@PathVariable("authorId") long authorId, @PathVariable("removedBookId") long bookId) {
        log.info("[PUT] Delete book with ID = {} for author with ID = {}", bookId, authorId);
        var author = authorsService.findById(authorId);
        var bookToRemove = author.getBooks().stream().filter(book -> book.getId() == bookId).findFirst();
        bookToRemove.ifPresent(it -> authorsService.removeBook(author, it));
        return "redirect:/authors/edit-form/" + author.getId();
    }
}