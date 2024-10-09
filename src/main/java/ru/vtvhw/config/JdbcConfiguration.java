package ru.vtvhw.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcConfiguration {

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.driverClassName}")
    private String jdbcDriverClass;

    @Value("${jdbc.initialSize}")
    private int jdbcInitialSize;

    @Value("${jdbc.maxActive}")
    private int jdbcMaxActive;

    @Value("${jdbc.username}")
    private String jdbcUser;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Bean
    public DataSource dataSource() {
        var dataSource = new BasicDataSource();
        dataSource.setDriverClassName(jdbcDriverClass);
        dataSource.setInitialSize(jdbcInitialSize);
        dataSource.setMaxTotal(jdbcMaxActive);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate namedJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }
}
