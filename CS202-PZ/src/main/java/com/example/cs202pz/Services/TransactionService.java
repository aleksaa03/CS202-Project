package com.example.cs202pz.Services;

import com.example.cs202pz.DbConfig.Queries.TransactionQuery;
import com.example.cs202pz.Entity.Transaction;
import com.example.cs202pz.Interfaces.ITransactionService;
import com.example.cs202pz.Requests.TransactionsRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TransactionService class provides services related to Transaction entity.
 */
public class TransactionService implements ITransactionService {
    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    /**
     * Constructs a TransactionService with the specified database connection.
     *
     * @param connection The database connection to be used for executing SQL queries.
     */
    public TransactionService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves transaction history for a given user and populates the provided TransactionsRequest object.
     *
     * @param request The TransactionsRequest object containing the user ID and an empty list for transactions.
     * @throws RuntimeException If an error occurs during the retrieval of transaction history.
     */
    public void getTransactions(TransactionsRequest request) {
        final String query = TransactionQuery.getTransactionByAccountId();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, request.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    request.getTransactions().add(new Transaction(
                            resultSet.getInt("Id"),
                            resultSet.getString("Date"),
                            resultSet.getDouble("Amount"),
                            resultSet.getDouble("AmountBefore"),
                            resultSet.getInt("Status")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a new transaction record into the database.
     *
     * @param transaction The Transaction object representing the transaction to be inserted.
     * @throws RuntimeException If an error occurs during the insertion of the transaction record.
     */
    public void insertTransaction(Transaction transaction) {
        final String query = TransactionQuery.insertTransaction();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transaction.getId());
            statement.setDouble(2, transaction.getAmount());
            statement.setDouble(3, transaction.getAmountBefore());
            statement.setInt(4, transaction.getStatus().ordinal());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
