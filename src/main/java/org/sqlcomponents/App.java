package org.sqlcomponents;

import org.example.MovieManager;
import org.example.model.Movie;
import org.example.store.MovieStore;
import static org.example.store.MovieStore.*;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws SQLException {
        // Lets get the manager of our database
        MovieManager movieManager = MovieManager.getManager(getDataSource());

        // Lets get the store
        MovieStore movieStore = movieManager.getMovieStore();

        Movie movie = new Movie();

        movie.setRating(2.3);
        movie.setGenre("Action");
        movie.setTitle("Inception");
        movie.setImdbId("23");
        movie.setYearOfRelease(((short) 2301));
        movie.setDirectedBy("Christopher Nolan");

        Movie ceateMovie = movieStore.insert().values(movie).returning();

        System.out.println("Movie Created " + ceateMovie.getId());

        // Select your data
        List<Movie> movies =  movieStore.select(
                directedBy().eq("Christopher Nolan")
        );

        // Print it
        movies.stream().forEach(m -> {
            System.out.println(m.getTitle() + "(" + m.getYearOfRelease() + ") - "+m.getRating()+" - " +m.getDirectedBy() );
        });

        movieStore.deleteAll();

    }

























    private static DataSource getDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerNames(new String[]{"localhost"});
        ds.setDatabaseName( "moviedb" );
        ds.setUser( "moviedb" );
        ds.setPassword( "moviedb" );
        return ds;
    }

}
