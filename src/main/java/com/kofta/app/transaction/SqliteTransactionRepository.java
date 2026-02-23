package com.kofta.app.transaction;

import com.kofta.app.common.repository.RepositoryException;
import com.kofta.app.common.result.Result;
import com.kofta.app.database.DatabaseConnectionManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SqliteTransactionRepository implements TransactionRepository {

    private DatabaseConnectionManager dbManager;

    public SqliteTransactionRepository(DatabaseConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    private Transaction mapToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
            UUID.fromString(rs.getString("id")),
            LocalDate.parse(rs.getString("date")),
            rs.getString("description"),
            rs.getBigDecimal("amount"),
            Category.fromString(rs.getString("category")).orElseThrow(),
            UUID.fromString(rs.getString("account_id"))
        );
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        var sql = "SELECT * FROM transactions WHERE id = ?";

        try (
            var connection = dbManager.getConnection();
            var stmt = connection.prepareStatement(sql)
        ) {
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
        var sql = "SELECT * FROM transactions";
        List<Transaction> res = new ArrayList<>();

        try (
            var connection = dbManager.getConnection();
            var stmt = connection.prepareStatement(sql)
        ) {
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
            "INSERT INTO transactions (id, date, description, amount, category, account_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (
            var connection = dbManager.getConnection();
            var stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, entity.id().toString());
            stmt.setString(2, entity.date().toString());
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
    public List<Transaction> findAll(TransactionFilter filter) {
        var sql = new StringBuilder("SELECT * FROM transactions WHERE 1=1");
        var params = new ArrayList<>();
        List<Transaction> res = new ArrayList<>();

        if (filter.accountId() != null) {
            sql.append(" AND account_id = ?");
            params.add(filter.accountId());
        }

        if (filter.category() != null) {
            sql.append(" AND category = ?");
            params.add(filter.category());
        }

        try (
            var connection = dbManager.getConnection();
            var stmt = connection.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

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
    public void deleteById(UUID id) {
        var sql = "DELETE FROM transactions WHERE id = ?";

        try (
            var connection = dbManager.getConnection();
            var stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
