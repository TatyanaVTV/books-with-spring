package ru.vtvhw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import ru.vtvhw.dao.BooksDao;
import ru.vtvhw.dao.InMemoryBooksDaoImpl;
import ru.vtvhw.dao.PostgreSqlDaoImpl;

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

        switch (dataSourceType) {
            case "db", "postgresql" -> {
                return new PostgreSqlDaoImpl();
            }
            default -> {
                return new InMemoryBooksDaoImpl();
            }
        }
    }
}