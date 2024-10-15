package ru.vtvhw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vtvhw.model.Author;
import ru.vtvhw.service.AuthorsService;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorsController {

    @Autowired
    private final AuthorsService authorsService;

    @GetMapping("/")
    public String authors(Model model) {
        model.addAttribute("authors", authorsService.getAllEntities());
        return "authors-list-form";
    }

    @GetMapping("/view")
    public String viewAuthor(@RequestParam("id") long authorId, Model model) {
        var author = authorsService.getEntity(authorId);
        model.addAttribute("author", author);
        model.addAttribute("books", author.getBooks());
        return "authors-view-form";
    }

    @GetMapping("/create-form")
    public String createForm() {
        return "authors-create-form";
    }

    @PostMapping("/create")
    public String createAuthor(Author author) {
        authorsService.createEntity(author);
        return "redirect:/authors/";
    }

    @GetMapping("/edit-form/{authorId}")
    public String editForm(@PathVariable("authorId") String authorId, Model model) {
        var author = authorsService.getEntity(Long.parseLong(authorId));
        model.addAttribute("author", author);
        model.addAttribute("books", author.getBooks());
        return "authors-edit-form";
    }

    @PutMapping("/update")
    public String updateAuthor(Author author) {
        authorsService.updateEntity(author);
        return "redirect:/authors/";
    }

    @PutMapping("/delete/{authorId}")
    public String deleteAuthor(@PathVariable("authorId") long authorId) {
        authorsService.deleteEntity(authorId);
        return "redirect:/authors/";
    }

    @PutMapping("/delete/{authorId}/book/{removedBookId}")
    public String deleteBookFromAuthor(@PathVariable("authorId") long authorId, @PathVariable("removedBookId") long bookId) {
        var author = authorsService.getEntity(authorId);
        var bookToRemove = author.getBooks().stream().filter(book -> book.getId() == bookId).findFirst();
        bookToRemove.ifPresent(it -> authorsService.removeBook(author, it));
        return "redirect:/authors/edit-form/" + author.getId();
    }
}