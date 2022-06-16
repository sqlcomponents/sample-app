package com.example.service;

import org.example.model.Movie;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class MovieService {

    /**
     * Data Source.
     */
    private final DataSource dataSource;

    /**
     * Creates MovieService.
     * @param theDataSource
     */
    public MovieService(final DataSource theDataSource) {
        this.dataSource = theDataSource;
    }

    /**
     * Creates a new Movie.
     * @param movie
     * @return createdMovie
     */
    public Movie create(final Movie movie) {
        return null;
    }

    /**
     * Reads Movie by Id.
     * @param id
     * @return movie
     */
    public Movie read(final Long id) {
        return null;
    }

    /**
     * Updates Movie By Id.
     * @param id
     * @param movie
     * @return updatedMovie
     */
    public Movie update(final Long id, final Movie movie) {
        return null;
    }

    /**
     * Deletes Movie by Id.
     * @param id
     * @return isDeleted
     */
    public boolean delete(final Long id) {
        return false;
    }
}
