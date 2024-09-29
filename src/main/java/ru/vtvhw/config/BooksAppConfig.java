package ru.vtvhw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vtvhw.dao.BooksDao;
import ru.vtvhw.dao.InMemoryBooksDaoImpl;

@Configuration
public class BooksAppConfig {

    @Bean
    public BooksDao booksDao() {
        return new InMemoryBooksDaoImpl();
    }
}
