package main.java.sample.covidportal.baza;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class BazaPodataka {
    private static final String DATABASE_CONFIGURATION_FILE = "src\\main\\resources\\database.properties";

    /**
     * Creates a new Database Connection and returns the database driver instance
     *
     * @return instance of database driver connection
     * @throws SQLException if there is an error on the database
     * @throws IOException if there is an error when making a query
     */

    public static Connection connectToDatabase() throws SQLException, IOException {
        Properties properties = new Properties();

        properties.load(new FileReader(DATABASE_CONFIGURATION_FILE));

        String url = properties.getProperty("databaseUrl");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

    /**
     * Closes the connection to database
     *
     * @param connection reference to instance of the connection
     * @throws SQLException if there is a problem when closing the connection
     */

    public static void closeConnectionToDatabase(Connection connection) throws SQLException {
        connection.close();
    }



}
