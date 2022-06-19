package com.example.service;

import org.example.model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.List;

import static com.example.service.constants.TestConstants.USER_NAME;

@SpringBootTest
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @BeforeEach
    void before() throws SQLException {
        cleanup();
    }

    @AfterEach
    void after() throws SQLException {
        cleanup();
    }

    private void cleanup() throws SQLException {
        movieService.delete(USER_NAME);
    }

    @Test
    void create() throws SQLException {
        final Movie movie = movieService.create(USER_NAME,
                anMovie());
        Assertions.assertTrue(movieService.read(USER_NAME, movie.getId()).isPresent(), "Created Movie");

    }

    @Test
    void update() throws SQLException {
        Movie movie = movieService.create(USER_NAME,
                anMovie());
        final Long newMovieId = movie.getId();
        movie.setTitle("maniMovie");

        movieService.update(USER_NAME, newMovieId, movie);
        movie = movieService.read(USER_NAME, newMovieId).get();
        Assertions.assertEquals("maniMovie", movie.getTitle(), "Updated Movie");
    }

    @Test
    void delete() throws SQLException {
        final Movie movie = movieService.create(USER_NAME,
                anMovie());
        movieService.delete(USER_NAME, movie.getId());
        Assertions.assertFalse(movieService.read(USER_NAME, movie.getId()).isPresent(), "Deleted Movie");
    }

    @Test
    void list() throws SQLException {
        final Movie movie = movieService.create(USER_NAME,
                anMovie());
        Movie newMovie = anMovie();
        movieService.create(USER_NAME, newMovie);
        Pageable pageable = PageRequest.of(1,4);
        List<Movie> listOfMovie = movieService.list(USER_NAME,pageable).toList();
        Assertions.assertEquals(2, listOfMovie.size());
    }

    Movie anMovie() {
        Movie movie = new Movie();
        movie.setTitle("beast");
        movie.setDirectedBy("nelson dhilipkumar");
        movie.setYearOfRelease(2022.0);
        movie.setGenre("action");
        movie.setRating(2.0);
        movie.setImdbId("IM" + System.currentTimeMillis());
        return movie;
    }
}