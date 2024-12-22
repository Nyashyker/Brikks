package be.kdg.integration.brikks_project;

import java.sql.*;
import java.util.Scanner;


public abstract class PlayerSave {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Student_1234";
    private static final String SERVER_URL = "jdbc:postgresql://localhost:5432/";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Player Management System!");


        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Create a new player");
            System.out.println("2. Select an existing player");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");


            int choice = scanner.nextInt();
            scanner.nextLine();


            switch (choice) {
                case 1 -> createPlayer(scanner);
                case 2 -> selectPlayer(scanner);
                case 3 -> {
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


    private static void createPlayer(Scanner scanner) {
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine();
        System.out.print("Enter player high score: ");
        int highScore = scanner.nextInt();


        String sql = "INSERT INTO players (player_id, player_name, player_highscore) VALUES (?, ?, ?)";


        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            int playerId = generatePlayerId();
            stmt.setInt(1, playerId);
            stmt.setString(2, playerName);
            stmt.setInt(3, highScore);


            stmt.executeUpdate();
            System.out.println("Player created successfully! Player ID: " + playerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void selectPlayer(Scanner scanner) {
        String sql = "SELECT player_id, player_name FROM players";


        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {


            System.out.println("\nExisting Players:");
            while (rs.next()) {
                int playerId = rs.getInt("player_id");
                String playerName = rs.getString("player_name");
                System.out.println(playerId + " - " + playerName);
            }


            System.out.print("\nEnter the ID of the player you want to select: ");
            int selectedPlayerId = scanner.nextInt();
            scanner.nextLine();


            loadPlayerInfo(selectedPlayerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static void loadPlayerInfo(int playerId) {
        String sql = "SELECT player_id, player_name FROM players WHERE player_id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String playerName = rs.getString("player_name");

                    System.out.println("\nPlayer Information:");
                    System.out.println("Player ID: " + playerId);
                    System.out.println("Player Name: " + playerName);
                    // Removed the high score display
                } else {
                    System.out.println("Player not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int generatePlayerId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }


    public abstract void save(Board board);
    public abstract void save(Bombs bombs);
    public abstract void save(Energy energy);
    public abstract void save(BonusScore bonusScore);
    public abstract void save(Player player);
    public abstract void save(byte row, byte column);
}



