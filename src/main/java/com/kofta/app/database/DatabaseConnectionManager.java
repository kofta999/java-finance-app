package com.kofta.app.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DatabaseConnectionManager implements AutoCloseable {

    private final String url;
    private Connection connection;

    public DatabaseConnectionManager(String url) {
        this.url = url;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (var conn = getConnection()) {
            runMigrations(conn);
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url);
        }

        return connection;
    }

    private void runMigrations(Connection conn) throws SQLException {
        try (
            var stream = getClass()
                .getClassLoader()
                .getResourceAsStream("schema.sql");
            var reader = new BufferedReader(new InputStreamReader(stream));
            var stmt = conn.createStatement();
        ) {
            String[] sqlStmts = reader
                .lines()
                .collect(Collectors.joining("\n"))
                .split("--");

            for (String sql : sqlStmts) {
                stmt.addBatch(sql);
            }

            stmt.executeBatch();
        } catch (IOException e) {
            throw new SQLException("Failed to read schema", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
