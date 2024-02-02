package com.example.cs202pz.DbConfig;

import com.example.cs202pz.Common.Constants;
import com.example.cs202pz.Services.AccountService;
import com.example.cs202pz.Services.ErrorLogService;
import com.example.cs202pz.Services.TransactionService;
import com.example.cs202pz.Services.UserService;

import java.sql.*;

/**
 * DbManager class responsible for managing database operations in the ATM application.
 */
public class DbManager {
    /**
     * Represents the unique account ID.
     */
    private static int accountID = 0;

    /**
     * Gets the current account ID.
     *
     * @return The current account ID.
     */
    public static int getAccountID() {
        return accountID;
    }

    /**
     * Sets the account ID.
     *
     * @param accountID The account ID to be set.
     */
    public static void setAccountID(int accountID) {
        DbManager.accountID = accountID;
    }

    /**
     * Represents the database URL.
     */
    public static final String URL = "jdbc:mysql://localhost/cs202pz";

    /**
     * Represents the connection status to the database.
     */
    private static boolean connected = false;

    /**
     * Checks if the application is connected to the database.
     *
     * @return True if connected, false otherwise.
     */
    public static boolean isConnected() {
        return connected;
    }

    /**
     * Represents the database connection.
     */
    public static Connection connection;

    /**
     * Establishes a connection to the database.
     */
    public static void connect() {
        if (isConnected()) return;
        try {
            connection = DriverManager.getConnection(URL, "root", "123123");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connected = true;
    }

    /**
     * Closes the connection to the database.
     */
    public static void disconnect() {
        if (!isConnected()) return;
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connected = false;
    }

    private static UserService userService = null;
    public static UserService userService() throws Exception {
        if (!connected) {
            throw new Exception(Constants.notConnected);
        }

        if (userService == null) {
            userService = new UserService(connection);
        }

        return userService;
    }

    private static AccountService accountService = null;
    public static AccountService accountService() throws Exception {
        if (!connected) {
            throw new Exception(Constants.notConnected);
        }

        if (accountService == null) {
            accountService = new AccountService(connection);
        }

        return accountService;
    }

    private static TransactionService transactionService = null;
    public static TransactionService transactionService() throws Exception {
        if (!connected) {
            throw new Exception(Constants.notConnected);
        }

        if (transactionService == null) {
            transactionService = new TransactionService(connection);
        }

        return transactionService;
    }

    private static ErrorLogService errorLogService = null;
    public static ErrorLogService errorLogService() throws Exception {
        if (!connected) {
            throw new Exception(Constants.notConnected);
        }

        if (errorLogService == null) {
            errorLogService = new ErrorLogService(connection);
        }

        return errorLogService;
    }
}