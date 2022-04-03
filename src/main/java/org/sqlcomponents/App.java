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
public class App
{
    public static void main(final String[] aArgs) throws SQLException
    {
	// Lets get the manager of our database
	//todo: call it as getInstance() as it has to once instance
	MovieManager movieManager = MovieManager.getManager(connect2DataSource());

	// Lets get the store
	MovieStore movieStore = movieManager.getMovieStore();

	Movie movie = new Movie();

	movie.setRating(2.3);
	movie.setGenre("Action");
	movie.setTitle("Inception");
	movie.setImdbId("23");
	movie.setYearOfRelease(((short) 2301));
	movie.setDirectedBy("Christopher Nolan");

	Movie createdMovie = movieStore.insert().values(movie).returning();

	System.out.println("Movie Created " + createdMovie.getId());

	List<Movie> movies = movieStore.select(directedBy().eq("Christopher Nolan"));

	printMovies(movies);

	movieStore.deleteAll();
    }

    //todo: why not have print() part of the generated code? nice debugging time help
    private static void printMovies(final List<Movie> aMovies)
    {
	aMovies.forEach(m -> {
	    System.out.println(m.getTitle() + "(" + m.getYearOfRelease() + ") - " + m.getRating() + " - " + m.getDirectedBy());
	});
    }

    private static final DataSource connect2DataSource()
    {
	PGSimpleDataSource ds = new PGSimpleDataSource();
	ds.setServerNames(new String[]{"localhost"});
	ds.setDatabaseName("moviedb");
	ds.setUser("moviedb");
	ds.setPassword("moviedb");
	return ds;
    }
}
