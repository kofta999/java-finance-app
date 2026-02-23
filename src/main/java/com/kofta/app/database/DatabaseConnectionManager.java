package com.kofta.app.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DatabaseConnectionManager implements AutoCloseable {

    private final HikariDataSource dataSource;

    public DatabaseConnectionManager(String url) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setAutoCommit(true);
        config.setConnectionTestQuery("SELECT 1");

        this.dataSource = new HikariDataSource(config);

        initializeDatabase();
    }

    private void initializeDatabase() {
        try (var conn = getConnection()) {
            runMigrations(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    /**
     * Gets a connection from the pool
     * IMPORTANT: Must be used in try-with-resources block to return to pool
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
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
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
