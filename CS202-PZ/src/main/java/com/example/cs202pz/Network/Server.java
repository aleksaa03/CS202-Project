package com.example.cs202pz.Network;

import com.example.cs202pz.Common.Constants;
import com.example.cs202pz.DbConfig.DbManager;
import com.example.cs202pz.Requests.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class manages the server-side functionality of the ATM application.
 * It handles client connections, data reception, and database interactions.
 */
public class Server {
    /**
     * The port number on which the server is listening for incoming connections.
     */
    public static final int PORT = 8080;

    /**
     * The IP address or hostname of the server.
     */
    public static final String IP = "localhost";

    /**
     * Indicates whether the server is up and running.
     */
    private static boolean up = false;

    /**
     * Checks if the server is up and running.
     *
     * @return True if the server is up, false otherwise.
     */
    public static boolean isUp() {
        return up;
    }

    /**
     * The server socket used for accepting incoming connections.
     */
    private static ServerSocket serverSocket;

    /**
     * Starts the server, listens for connections, and handles client requests.
     *
     * @throws Exception If an error occurs while starting the server.
     */
    public static void start() throws Exception {
        if (isUp()) return;
        System.out.println(Constants.serverIsStarting);
        serverSocket = new ServerSocket(PORT);
        System.out.println(Constants.serverStarted);
        up = true;

        DbManager.connect();

        System.out.println(Constants.serverIsWaitingForConnection);
        final Socket socket = serverSocket.accept();
        System.out.println(Constants.clientConnected);
        try (final Handler handler = new Handler(socket, false)) {
            while (isUp() && socket.isConnected()) {
                System.out.println(Constants.serverIsWaitingForData);
                final Object received = handler.tryReceive();
                System.out.println(Constants.serverReceivedData);

                System.out.println(Constants.serverHandlingData);
                if (received == null) break;

                System.out.println(Constants.serverSendingData);
                if (received instanceof TransactionsRequest request) {
                    DbManager.transactionService().getTransactions(request);
                    handler.send(request);
                } else if (received instanceof UserRequest request) {
                    DbManager.userService().getUser(request);
                    handler.send(request);
                } else if (received instanceof LoginRequest request) {
                    request.setId(DbManager.userService().login(request.getPin()));
                    handler.send(request);
                } else if (received instanceof DepositRequest request) {
                    if (request.isWithdrawal()) {
                        DbManager.accountService().withdraw(request);
                    } else {
                        DbManager.accountService().deposit(request);
                    }
                    handler.send(request);
                } else if (received instanceof ErrorLogRequest request) {
                    DbManager.errorLogService().insertExceptionLog(request.getMessage());
                }
            }
        }
        stop();
    }

    /**
     * Stops the server, closes the server socket, and disconnects from the database.
     *
     * @throws IOException If an I/O error occurs while stopping the server.
     */
    public static void stop() throws IOException {
        if (!isUp()) return;
        serverSocket.close();
        up = false;
        DbManager.disconnect();
    }

    /**
     * The main method to start the server.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        try {
            Server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}