package brikks.save;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;


public class DatabaseConnection implements AutoCloseable {
    private final Connection connection;
    private final Statement stmt;


    public DatabaseConnection(final String url, final String user, final String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
        this.stmt = this.connection.createStatement();
    }


    public void executeUpdate(final String sql) throws SQLException {
        this.stmt.executeUpdate(sql);
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        return this.stmt.getGeneratedKeys();
    }

    public ResultSet executeQuery(final String sql) throws SQLException {
        return this.stmt.executeQuery(sql);
    }


    @Override
    public void close() throws SQLException {
        this.stmt.close();
        this.connection.close();
    }
}
