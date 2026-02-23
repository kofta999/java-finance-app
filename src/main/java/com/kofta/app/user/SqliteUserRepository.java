package com.kofta.app.user;

import com.kofta.app.common.repository.RepositoryException;
import com.kofta.app.common.result.Result;
import com.kofta.app.database.DatabaseConnectionManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SqliteUserRepository implements UserRepository {

    private DatabaseConnectionManager dbManager;

    public SqliteUserRepository(DatabaseConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        return new User(
            UUID.fromString(rs.getString("id")),
            rs.getString("name")
        );
    }

    @Override
    public Optional<User> findById(UUID id) {
        var sql = "SELECT * FROM users WHERE id = ?";

        try (
            var connection = dbManager.getConnection();
            var stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        var sql = "SELECT * FROM users";
        List<User> res = new ArrayList<>();

        try (
            var connection = dbManager.getConnection();
            var stmt = connection.prepareStatement(sql)
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                res.add(mapToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        return res;
    }

    @Override
    public Result<Void, RepositoryException> save(User entity) {
        var sql = "INSERT INTO users (id, name) VALUES (?, ?)";

        try (
            var connection = dbManager.getConnection();
            var stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, entity.getId().toString());
            stmt.setString(2, entity.getName());
            stmt.executeUpdate();

            return new Result.Ok<>(null);
        } catch (SQLException e) {
            return new Result.Err<>(new RepositoryException(e));
        }
    }
}
