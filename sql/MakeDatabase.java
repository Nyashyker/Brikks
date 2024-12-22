import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MakeDatabase {
    public static void main(String[] args) {
        Connection con = DriverManager.getConnection("jdbc:postgresql:file:db/brikks");
        Statement stmt = con.createStatement();
        stmt.executeUpdate("CREATE TABLE players (name VARCHAR(3), score INTEGER)");
    }
}