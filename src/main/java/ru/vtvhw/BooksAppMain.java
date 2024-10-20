package ru.vtvhw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BooksAppMain {

    public static void main(String[] args) {
        log.info("Starting BookAppMain");
        log.debug("Some debug msg");
        SpringApplication.run(BooksAppMain.class, args);
    }
}