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
     * @param userName
     * @param movie
     * @return createdMovie
     */
    public Movie create(final String userName,
                        final Movie movie) {
        return null;
    }

    /**
     * Reads Movie by Id.
     * @param id
     * @return movie
     */
    public Movie read(final String userName,
                      final Long id) {
        return null;
    }

    /**
     * Updates Movie By Id.
     * @param id
     * @param movie
     * @return updatedMovie
     */
    public Movie update(final String userName,
                        final Long id, final Movie movie) {
        return null;
    }

    /**
     * Deletes Movie by Id.
     * @param id
     * @return isDeleted
     */
    public boolean delete(final String userName,
                          final Long id) {
        return false;
    }

    /**
     * Dletes all the movie.
     * @return noOfMoviesDeleted
     */
    public int delete(final String userName) {
        return 0;
    }
}
