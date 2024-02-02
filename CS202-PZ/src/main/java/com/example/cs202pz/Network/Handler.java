package com.example.cs202pz.Network;

import com.example.cs202pz.Requests.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handler class facilitates communication between a client and server in the ATM application.
 * Manages object input and output streams for sending and receiving requests.
 */
public class Handler implements AutoCloseable {
    /**
     * The output stream for sending objects to the connected entity.
     */
    private final ObjectOutputStream objectOutputStream;

    /**
     * The input stream for receiving objects from the connected entity.
     */
    private final ObjectInputStream objectInputStream;

    /**
     * Constructs a Handler for communication with the connected entity.
     *
     * @param socket    The socket representing the connection.
     * @param isClient  Indicates whether this handler is for a client (true) or server (false).
     * @throws IOException If an I/O error occurs while creating object input and output streams.
     */
    public Handler(final Socket socket, final boolean isClient) throws IOException {
        if (isClient) {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } else {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }
    }

    /**
     * Sends a Request object to the connected entity.
     *
     * @param object The Request object to be sent.
     * @throws IOException If an I/O error occurs while sending the object.
     */
    public void send(final Request object) throws IOException {
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    /**
     * Receives a Request object from the connected entity.
     *
     * @return The received Request object.
     * @throws IOException            If an I/O error occurs while receiving the object.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public Request receive() throws IOException, ClassNotFoundException {
        return (Request) objectInputStream.readObject();
    }

    /**
     * Attempts to receive a Request object from the connected entity.
     *
     * @return The received Request object, or null if an error occurs.
     */
    public Request tryReceive() {
        try {
            return receive();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the object input and output streams.
     *
     * @throws Exception If an error occurs while closing the streams.
     */
    @Override
    public void close() throws Exception {
        objectOutputStream.close();
        objectInputStream.close();
    }
}