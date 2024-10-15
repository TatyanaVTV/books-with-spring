package ru.vtvhw.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import ru.vtvhw.dao.AuthorsDao;
import ru.vtvhw.dao.BooksDao;
import ru.vtvhw.dao.hibernate.HibernateAuthorsDaoImpl;
import ru.vtvhw.dao.hibernate.HibernateBooksDaoImpl;
import ru.vtvhw.dao.memory.InMemoryAuthorsDaoImpl;
import ru.vtvhw.dao.memory.InMemoryBooksDaoImpl;
import ru.vtvhw.model.Author;
import ru.vtvhw.model.Book;

@Configuration
@Import({JdbcConfiguration.class, PropertiesConfig.class})
@ComponentScan("ru.vtvhw")
public class BooksAppConfig {

    @Autowired
    private Environment env;

    @Bean
    public BooksDao booksDao() {
        var dataSourceType = env.getProperty("data.source");
        dataSourceType = dataSourceType == null ? "memory" : dataSourceType;

        if (dataSourceType.equals("db")) {
            var batchSize = env.getProperty("hibernate.jdbc.batch_size", Integer.class, 50);
            return new HibernateBooksDaoImpl(sessionFactory(), batchSize);
        }
        return new InMemoryBooksDaoImpl();
    }

    @Bean
    public AuthorsDao authorsDao() {
        var dataSourceType = env.getProperty("data.source");
        dataSourceType = dataSourceType == null ? "memory" : dataSourceType;

        if (dataSourceType.equals("db")) {
            return new HibernateAuthorsDaoImpl(sessionFactory());
        }
        return new InMemoryAuthorsDaoImpl();
    }

    @Bean
    public SessionFactory sessionFactory() {
        var configuration = new org.hibernate.cfg.Configuration()
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(Author.class);
        return configuration.buildSessionFactory();
    }

    @Bean
    public InitialDataLoader initialDataLoader() {
        if (!env.getProperty("data.source", "memory").equals("db")) {
            var initialDataLoader = new InitialDataLoader(booksDao(), authorsDao());
            initialDataLoader.loadInitialDataAfterStartup();
        }
        return null;
    }
}