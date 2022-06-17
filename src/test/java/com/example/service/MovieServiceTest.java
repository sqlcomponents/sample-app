package com.example.service;

import static com.example.service.constants.TestConstants.*;
import org.example.model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @BeforeEach
    void before() {
        cleanup();
    }

    @AfterEach
    void after() {
        cleanup();
    }

    private void cleanup() {
        movieService.delete(USER_NAME);
    }

    @Test
    void create() {
        final Movie movie = movieService.create(USER_NAME,
                anMovie());
        Assertions.assertTrue(movieService.read(USER_NAME,movie.getId()).isPresent(),"Created Movie");

    }

    @Test
    void read() {
        final Movie movie = movieService.create("Manikanta",
                anMovie());
        Assertions.assertTrue(movieService.read("Manikanta",
                movie.getId()).isPresent(), "Movie created");
    }

    @Test
    void update() {
        final Movie movie=movieService.create("Manikanta",
                anMovie());
        final Long newMovieId = movie.getId();
        Movie newMovie=new Movie();
        Movie updatedMovie=movieService.update("Mani",newMovieId,newMovie);
        Assertions.assertEquals("maniMovie", updatedMovie.getTitle(), "updated");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            movieService.update("Mani",10000L,  newMovie);
        });
    }

    @Test
    void delete() {
        final Movie movie=movieService.create("Manikanta",
                anMovie());
        movieService.delete(USER_NAME,movie.getId());
        Assertions.assertFalse(movieService.read(USER_NAME,movie.getId()).isPresent(),"Deleted Movie");
    }

    @Test
    void list() {
        final Movie movie=movieService.create("Manikanta",
                anMovie());
        Movie newMovie=new Movie();
        movieService.create("Manikanta", newMovie);
        List<Movie> listOfMovie = movieService.list("Manikanta");
        Assertions.assertEquals(2, listOfMovie.size());
    }

    Movie anMovie() {
        Movie movie=new Movie();
        return movie;
    }
}