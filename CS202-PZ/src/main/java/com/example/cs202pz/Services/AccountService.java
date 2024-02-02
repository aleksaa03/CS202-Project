package com.example.cs202pz.Services;

import com.example.cs202pz.Common.Enums.TransactionStatus;
import com.example.cs202pz.DbConfig.DbManager;
import com.example.cs202pz.DbConfig.Queries.AccountQuery;
import com.example.cs202pz.Entity.Transaction;
import com.example.cs202pz.Interfaces.IAccountService;
import com.example.cs202pz.Requests.DepositRequest;

import java.sql.*;
import java.time.LocalDate;

/**
 * AccountService class provides services related to Account entity.
 */
public class AccountService implements IAccountService {
    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    /**
     * Constructs an AccountService with the specified database connection.
     *
     * @param connection The database connection to be used for executing SQL queries.
     */
    public AccountService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves the current account balance for a given account ID.
     *
     * @param id The account ID for which to retrieve the balance.
     * @return The current account balance.
     */
    public double getBalance(int id) {
        final String query = AccountQuery.getAccountById();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("Amount");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }

    /**
     * Processes a withdrawal transaction by updating the account balance and inserting a transaction record.
     *
     * @param request The DepositRequest object representing the withdrawal transaction.
     * @throws Exception If an error occurs during the withdrawal transaction.
     */
    public void withdraw(DepositRequest request) throws Exception {
        double balance = getBalance(request.getId());

        if (request.getAmount() <= 0 || balance - request.getAmount() < 0) {
            DbManager.transactionService().insertTransaction(new Transaction(request.getId(),
                    Date.valueOf(LocalDate.now()).toString(),
                    balance, balance, 2));
            request.setStatus(TransactionStatus.DECLINED);
            return;
        }

        final String query = AccountQuery.accountWithdraw();
        updateAmount(request, balance, query);
    }

    /**
     * Updates the account balance and inserts a transaction record based on the provided DepositRequest.
     *
     * @param request  The DepositRequest object representing the transaction.
     * @param balance  The current account balance before the transaction.
     * @param query    The SQL query for updating the account balance.
     * @throws Exception If an error occurs during the transaction.
     */
    public void updateAmount(DepositRequest request, double balance, String query) throws Exception {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, request.getAmount());
            statement.setInt(2, request.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            DbManager.transactionService().insertTransaction(new Transaction(request.getId(),
                    Date.valueOf(LocalDate.now()).toString(),
                    balance, balance, 2));
            request.setStatus(TransactionStatus.DECLINED);
            return;
        }
        DbManager.transactionService().insertTransaction(new Transaction(request.getId(),
                Date.valueOf(LocalDate.now()).toString(),
                getBalance(request.getId()), balance, 0));
        request.setStatus(TransactionStatus.APPROVED);
    }

    /**
     * Updates the account balance and inserts a transaction record for a deposit transaction.
     *
     * @param request The DepositRequest object representing the deposit transaction.
     * @throws Exception If an error occurs during the deposit transaction.
     */
    public void deposit(DepositRequest request) throws Exception {
        double balance = getBalance(request.getId());

        if (request.getAmount() <= 0) {
            DbManager.transactionService().insertTransaction(new Transaction(request.getId(),
                    Date.valueOf(LocalDate.now()).toString(),
                    balance, balance, 2));
            request.setStatus(TransactionStatus.DECLINED);
            return;
        }

        final String query = AccountQuery.accountDeposit();
        updateAmount(request, balance, query);
    }
}
