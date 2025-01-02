import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private final Connection connection;
    private final Statement stmt;

    public DatabaseConnection(final String url, final String user, final String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
        this.stmt = this.connection.createStatement();
    }

    public void write(final String command) throws SQLException {
        if (this.connection.isClosed()) {
            return;
        }

        this.stmt.executeUpdate(command);
    }

    public String read(final String command) throws SQLException {
        if (this.connection.isClosed()) {
            return null;
        }

        return this.stmt.executeQuery(command).toString();
    }
}
