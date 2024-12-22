import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_NAME = "ascii5";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Student_1234";
    private static final String SERVER_URL = "jdbc:postgresql://localhost:5432/";

    public DatabaseConnection() {
        Connection connection = null;

        try {
            System.out.println("Connecting to PostgreSQL server...");
            Connection serverConnection = DriverManager.getConnection(SERVER_URL, DB_USERNAME, DB_PASSWORD);


            System.out.println("Connecting to the database '" + DB_NAME + "'...");
            connection = DriverManager.getConnection(SERVER_URL + DB_NAME, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connection to database '" + DB_NAME + "' was successful!");

            Statement statement = connection.createStatement();




            String createPlayerTable = """
                CREATE TABLE IF NOT EXISTS PLAYER (
                    player_id SERIAL PRIMARY KEY,
                    player_name VARCHAR(255) NOT NULL,
                    player_highscore INT NOT NULL
                );
            """;

            statement.executeUpdate(createPlayerTable);

            String createGamesTable = """
                CREATE TABLE IF NOT EXISTS GAMES (
                    game_id SERIAL PRIMARY KEY,
                    game_duration INT NOT NULL,
                    start_datetime TIMESTAMP NOT NULL,
                    end_datetime TIMESTAMP NOT NULL
                );
            """;

            String createSavedScoreTable = """
                CREATE TABLE IF NOT EXISTS SAVED_SCORE (
                    save_id SERIAL PRIMARY KEY,
                    player_id SERIAL NOT NULL,
                    game_score SERIAL NOT NULL,
                    FOREIGN KEY (player_id) REFERENCES PLAYER(player_id)
                );
            """;

            String createSavedBoardsTable = """
                CREATE TABLE IF NOT EXISTS SAVED_BOARDS (
                    save_id SERIAL PRIMARY KEY,
                    game_id SERIAL NOT NULL,
                    board_x INT NOT NULL,
                    board_y INT NOT NULL,
                    board_energy INT NOT NULL,
                    block_id SERIAL,
                    bomb_id SERIAL,
                    FOREIGN KEY (game_id) REFERENCES GAMES(game_id)
                );
            """;

            String createSavedBlocksTable = """
                CREATE TABLE IF NOT EXISTS SAVED_BLOCKS (
                    block_id SERIAL PRIMARY KEY,
                    block_color VARCHAR(50) NOT NULL
                );
            """;

            String createSavedBombTable = """
                CREATE TABLE IF NOT EXISTS SAVED_BOMB (
                    bomb_id SERIAL PRIMARY KEY,
                    bomb_score INT NOT NULL
                );
            """;

            String createSavedCellsTable = """
                CREATE TABLE IF NOT EXISTS SAVED_CELLS (
                    cell_id SERIAL PRIMARY KEY,
                    game_id SERIAL NOT NULL,
                    cell_x INT NOT NULL,
                    cell_y INT NOT NULL,
                    FOREIGN KEY (game_id) REFERENCES GAMES(game_id)
                );
            """;

            statement.executeUpdate(createGamesTable);
            statement.executeUpdate(createSavedScoreTable);
            statement.executeUpdate(createSavedBlocksTable);
            statement.executeUpdate(createSavedBombTable);
            statement.executeUpdate(createSavedCellsTable);
            statement.executeUpdate(createSavedBoardsTable);

            System.out.println("Database schema created successfully!");

        } catch (SQLException e) {
            System.err.println("Failed to connect to the database. Please check your credentials and database setup.");
            e.printStackTrace();
        }
    }



}