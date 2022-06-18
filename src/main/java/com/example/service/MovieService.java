package com.example.service;

import org.example.model.Movie;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    /**
     * number THREE.
     */
    public static final int THREE = 3;
    /**
     * number FOUR.
     */
    public static final int FOUR = 4;
    /**
     * number FIVE.
     */
    public static final int FIVE = 5;
    /**
     * number SIX.
     */
    public static final int SIX = 6;
    /**
     * number TW0.
     */
    public static final int TWO = 2;
    /**
     * number ONE.
     */
    public static final int ONE = 1;
    /**
     * number SEVEN.
     */
    public static final int SEVEN = 7;
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
                        final Movie movie) throws SQLException {
       Movie insertedMovie = null;
       final String query = "INSERT INTO "
               + "movie(title,directed_by,year_of_release,rating,genre,imdb_id)"
               + " VALUES (? ,? ,? ,? , ? , ? )";

        try (java.sql.Connection dbConnection = dataSource.getConnection();
            PreparedStatement preparedStatement = dbConnection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
           preparedStatement.setString(ONE, movie.getTitle());
           preparedStatement.setString(TWO, movie.getDirectedBy());
           preparedStatement.setDouble(THREE, movie.getYearOfRelease());
           preparedStatement.setDouble(FOUR, movie.getRating());
           preparedStatement.setString(FIVE, movie.getGenre());
           preparedStatement.setString(SIX, movie.getImdbId());

           if (preparedStatement.executeUpdate() == ONE) {
               ResultSet res = preparedStatement.getGeneratedKeys();
               if (res.next()) {
                   insertedMovie = read(userName, res.getLong(ONE)).get();
               }
           }

       }

       return insertedMovie;
    }

    /**
     * Reads Movie by Id.
     * @param userName
     * @param id
     * @return movie
     */
    public Optional<Movie> read(final String userName,
                               final Long id) throws SQLException {
        Movie movie = null;
        final String query =
                "SELECT id,title,directed_by,year_of_release,"
                + "rating,genre,imdb_id"
                + " FROM movie WHERE id = ?";
        try (java.sql.Connection dbConnection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = dbConnection.prepareStatement(query)) {
            preparedStatement.setLong(ONE, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                movie = rowMapper(resultSet);
            }
         }
        return Optional.ofNullable(movie);
    }

    private Movie rowMapper(final ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();
        movie.setId(resultSet.getLong(ONE));
        movie.setTitle(resultSet.getString(TWO));
        movie.setDirectedBy(resultSet.getString(THREE));
        movie.setYearOfRelease(resultSet.getDouble(FOUR));
        movie.setRating(resultSet.getDouble(FIVE));
        movie.setGenre(resultSet.getString(SIX));
        movie.setImdbId(resultSet.getString(SEVEN));
        return movie;

    }

    /**
     * Updates Movie By Id.
     * @param userName
     * @param id
     * @param movie
     * @return updatedMovie
     */
    public Movie update(final String userName,
                        final Long id, final Movie movie) throws SQLException {
            Movie updatedmovie = null;
            final String query = "UPDATE movie SET title = ? ,"
                    + "directed_by = ? ,year_of_release = ?"
                    + ",rating = ?, genre = ?,imdb_id = ? where id = ?";

            try (java.sql.Connection dbConnection = dataSource.getConnection();
                PreparedStatement preparedStatement = dbConnection
                        .prepareStatement(query, Statement
                                .RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(ONE, movie.getTitle());
                preparedStatement.setString(TWO, movie.getDirectedBy());
                preparedStatement.setDouble(THREE, movie.getYearOfRelease());
                preparedStatement.setDouble(FOUR, movie.getRating());
                preparedStatement.setString(FIVE, movie.getGenre());
                preparedStatement.setString(SIX, movie.getImdbId());

                preparedStatement.setLong(SEVEN, id);
                if (preparedStatement.executeUpdate() == ONE) {
                    ResultSet res = preparedStatement.getGeneratedKeys();
                    if (res.next()) {
                        updatedmovie = read(userName, res.getLong(ONE)).get();
                    }
                }
            }

        return updatedmovie;

    }

    /**
     * Deletes Movie by Id.
     * @param userName
     * @param id
     * @return isDeleted
     */
    public boolean delete(final String userName,
                          final Long id) throws SQLException {
        boolean isDeleted = false;

        final String query = "Delete from movie where id =?";
        try (Connection dbConnection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement
                         = dbConnection.prepareStatement(query)) {
                preparedStatement.setLong(ONE, id);


                isDeleted = preparedStatement.executeUpdate() == ONE;
            }
        }

        return isDeleted;
    }

    /**
     * Dletes all the movie.
     * @param userName
     * @return noOfMoviesDeleted
     */
    public int delete(final String userName) throws SQLException {
        int noOfDeletedMovies = 0;

        final String query = "DELETE FROM movie";
        try (java.sql.Connection dbConnection = dataSource.getConnection();
             PreparedStatement preparedStatement = dbConnection.
                                                    prepareStatement(query)) {

            noOfDeletedMovies = preparedStatement.executeUpdate();


        }

        return noOfDeletedMovies;
    }

    /**
     * Lists all the movies.
     * @param userName
     * @return movies
     */
    public List<Movie> list(final String userName) throws SQLException {
        List<Movie> movies = new ArrayList<>();

        final String query = "SELECT id,title,directed_by ,year_of_release "
                + ",rating ,genre ,imdb_id FROM movie";
        try (java.sql.Connection dbConnection = dataSource.getConnection();
        PreparedStatement preparedStatement = dbConnection
                        .prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                movies.add(rowMapper(resultSet));
            }
        }
        return movies;
    }
}
