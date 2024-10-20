package ru.vtvhw.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.vtvhw.exceptions.AuthorNotFoundException;
import ru.vtvhw.exceptions.BookNotFoundException;
import ru.vtvhw.exceptions.EntityNotFoundException;

@ControllerAdvice
@RequestMapping("/error")
@Slf4j
public class ErrorsPageController implements ErrorController {

    public ErrorsPageController() {
        log.debug("ErrorsController initialized");
    }

    @GetMapping
    @ExceptionHandler({
            AuthorNotFoundException.class,
            BookNotFoundException.class,
            NumberFormatException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class
    })
    public String handleGetError(HttpServletRequest request, Throwable exception, Model model) {
        var errorMsg = defineErrorMsgForUI(exception);
        log.error(errorMsg);
        model.addAttribute("errorMsg", errorMsg);
        return "errors";
    }

    private String defineErrorMsgForUI(Throwable exception) {
        String msg = "Internal server error";
        if (exception instanceof EntityNotFoundException || exception instanceof IllegalArgumentException) {
            msg = exception.getLocalizedMessage();
        }
        if (exception instanceof NumberFormatException || exception instanceof MethodArgumentTypeMismatchException) {
            msg = "Wrong argument: ID must be a number and can only contain numbers!";
        }
        return msg;
    }
}