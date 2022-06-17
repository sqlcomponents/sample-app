package com.example.service;

import org.example.model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        movieService.delete();
    }

    @Test
    void create() {
        final Movie movie = movieService.create("mani",
                anMovie());
        Assertions.assertTrue(movieService.read("mani",movie.id()).isPresent(),"Created Movie");

    }

    @Test
    void read() {
        final Movie movie = movieService.create("Manikanta",
                anMovie());
        Assertions.assertTrue(movieService.read("Manikanta",
                movie.id()).isPresent(), "Movie created");
    }

    @Test
    void update() {
        final Movie movie=movieService.create("Manikanta",
                anMovie());
        final Long newMovieId = movie.id();
        Movie newMovie=new Movie(null, "maniMovie", "An Movie",
                null,null,null,null);
        Movie updatedMovie=movieService.update(newMovieId,
                "Mani",newMovie);
        Assertions.assertEquals("maniMovie", updatedMovie.title(), "updated");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            movieService.update(10000L, "Mani", newMovie);
        });
    }

    @Test
    void delete() {
        final Movie movie=movieService.create("Manikanta",
                anMovie());
        movieService.delete("mani",movie.id());
        Assertions.assertFalse(movieService.read("mani",movie.id()).isPresent(),"Deleted Movie");
    }

    @Test
    void list() {
        final Movie movie=movieService.create("Manikanta",
                anMovie());
        Movie newMovie=new Movie(null, "tom", "An Movie",
                null, null, null, null);
        movieService.create("Manikanta", newMovie);
        List<Movie> listOfMovie = movieService.list("Manikanta");
        Assertions.assertEquals(2, listOfMovie.size());
    }

    Movie anMovie() {
        Movie movie=new Movie();
        return movie;
    }
}