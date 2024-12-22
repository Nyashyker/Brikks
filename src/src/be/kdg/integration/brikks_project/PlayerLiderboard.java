import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.DriverManager;
import java.util.Scanner;

public class PlayerLiderboard {

    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Student_1234";
    private static final String SERVER_URL = "jdbc:postgresql://localhost:5432/";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Leaderboard Game!");




        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Log a high score");
            System.out.println("2. View top 5 high scores");
            System.out.println("3. View a player's top 5 scores");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");




            int choice = scanner.nextInt();
            scanner.nextLine();



            switch (choice) {
                case 1 -> logScore(scanner);
                case 2 -> displayTopScores();
                case 3 -> filterScoresByPlayer(scanner);
                case 4 -> {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }




    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(SERVER_URL, DB_USERNAME, DB_PASSWORD);
    }



    private static void logScore(Scanner scanner) {
        System.out.print("Enter player ID: ");
        int playerId = scanner.nextInt();
        System.out.print("Enter game score: ");
        int gameScore = scanner.nextInt();




        String sql = "INSERT INTO saved_scores (save_id, player_id, game_score) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {




            stmt.setInt(1, generateSaveId());
            stmt.setInt(2, playerId);
            stmt.setInt(3, gameScore);
            stmt.executeUpdate();




            System.out.println("High score logged successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    private static void displayTopScores() {
        String sql = """
        SELECT p.player_name, sc.game_score, g.start_datetime
        FROM saved_scores sc
        JOIN players p ON sc.player_id = p.player_id
        JOIN saved_boards sb ON sb.save_id = sc.game_id
        JOIN games g ON g.game_id = sb.game_id
        ORDER BY sc.game_score DESC
        LIMIT 5
      """;




        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {




            System.out.println("\nTop 5 High Scores:");
            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int gameScore = rs.getInt("game_score");
                Timestamp startDatetime = rs.getTimestamp("start_datetime");
                System.out.println(playerName + " - " + gameScore + " - " + startDatetime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    private static void filterScoresByPlayer(Scanner scanner) {
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine();




        String sql = """
        SELECT p.player_name, sc.game_score, g.start_datetime
        FROM saved_scores sc
        JOIN players p ON sc.player_id = p.player_id
        JOIN saved_boards sb ON sb.save_id = sc.game_id
        JOIN games g ON g.game_id = sb.game_id
        WHERE p.player_name = ?
        ORDER BY sc.game_score DESC
        LIMIT 5
      """;




        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {




            stmt.setString(1, playerName);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\nTop 5 Scores for " + playerName + ":");
                boolean hasScores = false;
                while (rs.next()) {
                    hasScores = true;
                    String name = rs.getString("player_name");
                    int gameScore = rs.getInt("game_score");
                    Timestamp startDatetime = rs.getTimestamp("start_datetime");
                    System.out.println(name + " - " + gameScore + " - " + startDatetime);
                }
                if (!hasScores) {
                    System.out.println("No scores found for this player.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private static int generateSaveId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}

