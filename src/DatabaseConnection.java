import java.sql.*;

public class DatabaseConnection implements AutoCloseable {
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

    public ResultSet read(final String command) throws SQLException {
        if (this.connection.isClosed()) {
            return null;
        }

        return this.stmt.executeQuery(command);
    }

    @Override
    public void close() throws SQLException {
        this.stmt.close();
        this.connection.close();
    }
}
