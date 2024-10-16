package ru.vtvhw.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PropertiesConfig.class)
@ComponentScan("ru.vtvhw")
public class BooksAppConfig {
}