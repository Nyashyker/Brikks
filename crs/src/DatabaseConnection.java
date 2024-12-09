

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    /
    private static final String DB_NAME = "ascii5";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Student_1234";


    private static final String SERVER_URL = "jdbc:postgresql://localhost:5432/" ;

    public DatabaseConnection() {
        Connection connection = null;

        try {

            System.out.println("Connecting to PostgreSQL server...");
            Connection serverConnection = DriverManager.getConnection(SERVER_URL, DB_USERNAME, DB_PASSWORD);

            //  Create the database
            createDatabase(serverConnection);

            // Connect to the created database
            System.out.println("Connecting to the database '" + DB_NAME + "'...");
            connection = DriverManager.getConnection(SERVER_URL + DB_NAME, DB_USERNAME, DB_PASSWORD);

            System.out.println("Connection to database '" + DB_NAME + "' was successful!");

            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS"
                    + " players (player_name VARCHAR(10),"
                    + "          top_score INTEGER)");
            statement.executeUpdate("INSERT INTO"
                    + " players (player_name, top_score)"
              + " VALUES ('Rado', 3214)");


        } catch (SQLException e) {

            System.err.println("Failed to connect to the database. Please check your credentials and database setup.");
            e.printStackTrace();
        }
    }

    private static void createDatabase(Connection serverConnection) {
        try (Statement statement = serverConnection.createStatement()) {
            String createDatabaseSQL = "CREATE DATABASE " + DB_NAME;
            statement.executeUpdate(createDatabaseSQL);
            System.out.println("Database '" + DB_NAME + "' created successfully!");
        } catch (Exception e) {
        }
    }
}

