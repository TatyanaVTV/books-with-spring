package ru.vtvhw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.vtvhw.dao.BooksDao;
import ru.vtvhw.dao.InMemoryBooksDaoImpl;

@Configuration
@PropertySource("classpath:application.properties")
public class BooksAppConfig {

    @Bean
    public BooksDao booksDao() {
        return new InMemoryBooksDaoImpl();
    }
}
