package com.example.cs202pz.Services;

import com.example.cs202pz.DbConfig.Queries.AccountQuery;
import com.example.cs202pz.DbConfig.Queries.UserQuery;
import com.example.cs202pz.Interfaces.IUserService;
import com.example.cs202pz.Requests.UserRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserService class provides services related to User entity.
 */
public class UserService implements IUserService {
    /**
     * The database connection used for executing SQL queries.
     */
    private final Connection connection;

    /**
     * Constructs a UserService with the specified database connection.
     *
     * @param connection The database connection to be used for executing SQL queries.
     * @throws RuntimeException If an error occurs during the retrieval of user information.
     */
    public UserService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves user information for a given user and populates the provided UserRequest object.
     *
     * @param request The UserRequest object containing the user ID and empty user information.
     */
    public void getUser(UserRequest request) {
        final String query = UserQuery.getUserByAccountId();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, request.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    request.setUser(resultSet.getString("Firstname"), resultSet.getString("Lastname"), resultSet.getDouble("Amount"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempts to log in a user by verifying the provided PIN against the database.
     *
     * @param pin The PIN entered by the user for login.
     * @return The account ID if login is successful, -1 otherwise.
     * @throws RuntimeException If an error occurs during the login process.
     */
    public int login(String pin) {
        final String query = AccountQuery.getAccountByPin();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pin);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
