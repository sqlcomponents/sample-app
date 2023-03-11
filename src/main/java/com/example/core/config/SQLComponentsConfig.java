package com.example.core.config;

import org.example.MovieManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configuration for SQL Components.
 */
@Configuration
public class SQLComponentsConfig {
    /**
     * Build Movie Manager.
     * @param dataSource
     * @return movieManager
     */
    @Bean
    MovieManager movieManager(final DataSource dataSource) {
        return MovieManager.getManager(dataSource);
    }
}
