package com.example;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.example.MovieManager;
import org.example.store.RolesStore;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws SQLException {
        MovieManager movieManager = MovieManager.getManager(getDataSource());

        RolesStore rolesStore = movieManager.getRolesStore();

        rolesStore.select().forEach(System.out::println);
    }


















    private static DataSource getDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3308/sample-db?allowPublicKeyRetrieval=true");
        ds.setDatabaseName( "sample-db" );
        ds.setUser( "mysql-user" );
        ds.setPassword( "password" );
        return ds;
    }

}
