package com.kofta.app.transaction;

import com.kofta.app.common.repository.RepositoryException;
import com.kofta.app.common.result.Result;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class SqliteTransactionRepository implements TransactionRepository {

    private Connection connection;

    SqliteTransactionRepository(Connection connection) {
        this.connection = connection;
    }

    private Transaction mapToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
            UUID.fromString(rs.getString("id")),
            rs.getDate("date").toLocalDate(),
            rs.getString("description"),
            rs.getBigDecimal("amount"),
            Category.fromString(rs.getString("category")).orElseThrow(),
            UUID.fromString(rs.getString("account_id"))
        );
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        var sql = "SELECT * FROM Transaction WHERE id = ?";

        try (var stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapToTransaction(rs));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        var sql = "SELECT * FROM Transaction";
        List<Transaction> res = new ArrayList<>();

        try (var stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                res.add(mapToTransaction(rs));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        return res;
    }

    @Override
    public Result<Void, RepositoryException> save(Transaction entity) {
        var sql =
            "INSERT INTO Transaction(id, date, description, amount, category, account_id) VALUES ?, ?, ?, ?, ?, ?";

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.id().toString());
            stmt.setDate(2, Date.valueOf(entity.date()));
            stmt.setString(3, entity.description());
            stmt.setBigDecimal(4, entity.amount());
            stmt.setString(5, entity.category().toString());
            stmt.setString(6, entity.accountId().toString());

            stmt.executeUpdate();

            return new Result.Ok<>(null);
        } catch (SQLException e) {
            return new Result.Err<>(new RepositoryException(e));
        }
    }

    @Override
    public List<Transaction> findAll(Predicate<Transaction> predicate) {
        // TODO: Either remove this method with something simpler or... good luck out there
        // Simple implementation to save time

        return findAll().stream().filter(predicate).toList();
    }

    @Override
    public void deleteById(UUID id) {
        var sql = "DELETE FROM Transaction WHERE id = ?";

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
