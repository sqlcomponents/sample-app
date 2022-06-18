package com.example.controllers;

import com.example.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.model.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies", description = "Resource to manage movies")
class MovieAPIcontroller {
    /**
     * Original server.
     */
    private final MovieService movieService;

    /**
     * MovieAPIcontroller.
     * @param theMovieService
     */
    MovieAPIcontroller(final MovieService theMovieService) {
        this.movieService = theMovieService;
    }

    @PostMapping
    @Operation(summary = "create a new movie")
    public ResponseEntity<Movie> create(final Principal principal,
                                        @RequestBody final Movie movie)
                                        throws SQLException {
        return ResponseEntity.ok(movieService.create(
                principal.getName(), movie));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a movie by id")
    public ResponseEntity<Movie> read(final Principal principal,
                                        @PathVariable final Long id)
                                        throws SQLException {
        return ResponseEntity.of(movieService.read(principal.getName(), id));
    }

    @PutMapping ("/{id}")
    @Operation (summary = "put a movie by id")
    public ResponseEntity<Movie> update(final Principal principal,
                                        @PathVariable final Long id,
                                        final Movie movie)
                                        throws SQLException {
        return ResponseEntity.ok(movieService.update(
                principal.getName(), id, movie));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "deletes a movie by id")
    public ResponseEntity delete(final Principal principal,
                                 @PathVariable final Long id)
                                    throws SQLException {
        return movieService.delete(principal.getName(), id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "List a movie")
    public ResponseEntity<List<Movie>> list(final Principal principal)
            throws SQLException {
        return ResponseEntity.ok(movieService.list(principal.getName()));
    }
}





