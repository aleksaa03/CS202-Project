package com.example.cs202pz.Services;

import com.example.cs202pz.DbConfig.Queries.ErrorLogQuery;
import com.example.cs202pz.Interfaces.IErrorLogService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ErrorLogService class provides services related to ErrorLog entity.
 */
public class ErrorLogService implements IErrorLogService {
    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    /**
     * Constructs an ErrorLogService with the specified database connection.
     *
     * @param connection The database connection to be used for executing SQL queries.
     */
    public ErrorLogService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts an exception log entry with the provided error message into the database.
     *
     * @param message The error message to be logged.
     * @throws RuntimeException If an error occurs during the insertion of the exception log entry.
     */
    public void insertExceptionLog(String message) {
        final String query = ErrorLogQuery.insertErrorLog();

        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
