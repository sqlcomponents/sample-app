package com.example.service;

import org.example.MovieManager;
import org.example.model.Movie;
import org.example.store.MovieStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.sql.SQLException;
import java.util.Optional;

@Service
public class MovieService {

    /**
     * MovieStore.
     */
    private final MovieStore movieStore;

    /**
     * Creates MovieService.
     *
     * @param movieManager
     */
    public MovieService(final MovieManager movieManager) {
        this.movieStore = movieManager.getMovieStore();
    }

    /**
     * Creates a new Movie.
     *
     * @param userName
     * @param movie
     * @return createdMovie
     */
    public Movie create(final String userName,
                        final Movie movie) throws SQLException {
        return this.movieStore.insert().values(movie).returning();
    }

    /**
     * Reads Movie by Id.
     *
     * @param userName
     * @param id
     * @return movie
     */
    public Optional<Movie> read(final String userName,
                                final Long id) throws SQLException {
        return this.movieStore.select(id);
    }

    /**
     * Updates Movie By Id.
     *
     * @param userName
     * @param id
     * @param movie
     * @return updatedMovie
     */
    public Movie update(final String userName,
                        final Long id, final Movie movie) throws SQLException {
        this.movieStore.update(movie);
        return read(userName, movie.getId()).get();

    }

    /**
     * Deletes Movie by Id.
     *
     * @param userName
     * @param id
     * @return isDeleted
     */
    public boolean delete(final String userName,
                          final Long id) throws SQLException {
        return this.movieStore.delete(id) == 1;
    }

    /**
     * Dletes all the movie.
     *
     * @param userName
     * @return noOfMoviesDeleted
     */
    public int delete(final String userName) throws SQLException {
        return this.movieStore.delete().execute();
    }

    /**
     * Lists all the movies.
     *
     * @param userName
     * @param pageable
     * @return movies
     */
    public Page<Movie> list(final String userName,
                            final Pageable pageable) throws SQLException {
        MovieManager.Page<Movie> mPage = this.movieStore.select()
                .limit(pageable.getPageSize())
                .offset(pageable.getPageNumber() - 1).execute();
        return new PageImpl(mPage.getContent(), pageable,
                mPage.getTotalElements());
    }
}
