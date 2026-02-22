package com.kofta.app.account;

import com.kofta.app.common.repository.RepositoryException;
import com.kofta.app.common.result.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SqliteAccountRepository implements AccountRepository {

    private Connection connection;

    public SqliteAccountRepository(Connection connection) {
        this.connection = connection;
    }

    private Account mapToAccount(ResultSet rs) throws SQLException {
        return new Account(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getString("currency"),
            UUID.fromString(rs.getString("user_id"))
        );
    }

    @Override
    public Optional<Account> findById(UUID id) {
        var sql = "SELECT * FROM accounts WHERE id = ?";

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapToAccount(rs));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Account> findAll() {
        var sql = "SELECT * FROM accounts";
        List<Account> res = new ArrayList<>();

        try (var stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                res.add(mapToAccount(rs));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        return res;
    }

    @Override
    public Result<Void, RepositoryException> save(Account entity) {
        var sql =
            "INSERT INTO accounts (id, name, currency, user_id) VALUES (?, ?, ?, ?)";

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getId().toString());
            stmt.setString(2, entity.getName());
            stmt.setString(3, entity.getCurrency());
            stmt.setString(4, entity.getUserId().toString());

            stmt.executeUpdate();

            return new Result.Ok<>(null);
        } catch (SQLException e) {
            return new Result.Err<>(new RepositoryException(e));
        }
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        var sql = "SELECT * FROM accounts WHERE user_id = ?";
        List<Account> res = new ArrayList<>();

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                res.add(mapToAccount(rs));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        return res;
    }
}
