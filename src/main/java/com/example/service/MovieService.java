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
           preparedStatement.setString(1, movie.getTitle());
           preparedStatement.setString(2, movie.getDirectedBy());
           preparedStatement.setDouble(3, movie.getYearOfRelease());
           preparedStatement.setDouble(4, movie.getRating());
           preparedStatement.setString(5, movie.getGenre());
           preparedStatement.setString(6, movie.getImdbId());

           if (preparedStatement.executeUpdate() == 1) {
               ResultSet res = preparedStatement.getGeneratedKeys();
               if (res.next()) {
                   insertedMovie = read(userName, res.getLong(1)).get();
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
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                movie = rowMapper(resultSet);
            }
         }
        return Optional.ofNullable(movie);
    }

    private Movie rowMapper(final ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();
        movie.setId(resultSet.getLong(1));
        movie.setTitle(resultSet.getString(2));
        movie.setDirectedBy(resultSet.getString(3));
        movie.setYearOfRelease(resultSet.getDouble(4));
        movie.setRating(resultSet.getDouble(5));
        movie.setGenre(resultSet.getString(6));
        movie.setImdbId(resultSet.getString(7));
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
        Movie updatedMovie = null ;
        final String query = "UPDATE movie SET title = ? ,directed_by = ? ,year_of_release = ? ,rating = ? ,genre = ? ,imdb_id = ? WHERE id = ?";

        try (java.sql.Connection dbConnection = dataSource.getConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDirectedBy());
            preparedStatement.setDouble(3, movie.getYearOfRelease());
            preparedStatement.setDouble(4, movie.getRating());
            preparedStatement.setString(5, movie.getGenre());
            preparedStatement.setString(6, movie.getImdbId());

            preparedStatement.setLong(7, id);
            if( preparedStatement.executeUpdate() == 1 ) {
                ResultSet res = preparedStatement.getGeneratedKeys();
                if (res.next()) {
                    updatedMovie =  read(userName,res.getLong(1)).get();
                }
            }
        }
        return updatedMovie;
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
                preparedStatement.setLong(1, id);


                isDeleted = preparedStatement.executeUpdate() == 1;
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
             PreparedStatement preparedStatement = dbConnection.prepareStatement(query)){

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

        final String query = "SELECT id,title,directed_by ,year_of_release ,rating ,genre ,imdb_id FROM movie";
        try (java.sql.Connection dbConnection = dataSource.getConnection();
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query)){

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                movies.add(rowMapper(resultSet));

            }
        }
        return movies;
    }
}
