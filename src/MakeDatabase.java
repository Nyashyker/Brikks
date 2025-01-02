import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class MakeDatabase {
    public static void main(String[] args) {
        try (
                Connection con = DriverManager.getConnection(
                        "jdbc:postgresql:file:db/brikks",
                        "postgres",
                        "Student_1234"
                );
                Statement stmt = con.createStatement()
        ) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS players (name VARCHAR(3), score INTEGER)");
        } catch (SQLException sql_e) {
            System.out.println(sql_e.getMessage());
        }
    }
}
