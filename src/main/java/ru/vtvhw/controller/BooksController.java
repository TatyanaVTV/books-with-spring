package ru.vtvhw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vtvhw.service.BooksService;

import static java.lang.String.format;

@RestController
public class BooksController {
    private final BooksService booksService;

    @Autowired
    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @RequestMapping("/books")
    public String books() {
        var srcBuilder = new StringBuilder("<h1>List of Books:</h1>");
        srcBuilder.append("<table border=\"1\"><tr>");
        srcBuilder.append("<th align=\"center\" width=\"50px\">ID</th>");
        srcBuilder.append("<th align=\"center\" width=\"350px\">Title</th>");
        srcBuilder.append("<th align=\"center\" width=\"250px\">Author</th>");
        srcBuilder.append("<th align=\"center\" width=\"150px\">Genre</th>");
        srcBuilder.append("<th align=\"center\" width=\"50px\">Pages</th></tr>");

        booksService.getAllBooks()
                .forEach(book -> {
                    srcBuilder.append("<tr>");
                    srcBuilder.append(format("<td align=\"center\">%d</td>", book.getId()));
                    srcBuilder.append("<td align=\"center\">");
                    srcBuilder.append(format("<a href=\"/bookDetails?id=%d\">%s</a></td>", book.getId(), book.getTitle()));
                    srcBuilder.append(format("<td align=\"center\">%s</td>", book.getAuthor()));
                    srcBuilder.append(format("<td align=\"center\">%s</td>", book.getGenre()));
                    srcBuilder.append(format("<td align=\"center\">%s</td></tr>", book.getNumberOfPages()));
                });
        srcBuilder.append("</table><br><br><a href=\"/\"><< Back</a>");

        srcBuilder.append(addNewBookFormHtml());

        return srcBuilder.toString();
    }

    private String addNewBookFormHtml() {
        return """
                <br><hr><br>
                <div>
                <h2>Add new book:</h2>
                <form>
                    <table>
                        <tr>
                            <td><label for="title">Title:</label></td>
                            <td><input id="title" type="text" name="title" maxlength="100" required/></td>
                        </tr>
                        <tr>
                            <td><label for="author">Author: </label></td>
                            <td><input id="author" type="text" name="author" maxlength="50" required/></td>
                        </tr>
                        <tr>
                            <td><label for="genre">Genre: </label></td>
                            <td><input id="genre" type="text" name="genre" maxlength="50"/></td>
                        </tr>
                        <tr>
                            <td><label for="pagesNb">Number of pages: </label></td>
                            <td><input type="number" max="3000" id="pagesNb" name="pagesNb"/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td align="right"><input id="saveBookBtn" type="submit" /></td>
                        </tr>
                    </table>
                    <br>
                </form>
                </div>
                <hr><br><br><a href="/"><< Back</a>
                """.trim();
    }
}