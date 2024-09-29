package ru.vtvhw.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "Greetings! Books can be found here: <a href=\"/books\">List of Books</a>";
    }
}