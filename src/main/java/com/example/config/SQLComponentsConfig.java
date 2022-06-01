package com.example.config;

import org.example.MovieManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SQLComponentsConfig {
    @Bean
    MovieManager movieManager(final DataSource dataSource) {
        return MovieManager.getManager(dataSource);
    }
}
