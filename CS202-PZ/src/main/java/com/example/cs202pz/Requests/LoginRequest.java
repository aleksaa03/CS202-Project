package com.example.cs202pz.Requests;

/**
 * LoginRequest class represents a request to log in with a PIN in the ATM application.
 */
public class LoginRequest extends Request {
    private final String pin;
    private int id;

    public LoginRequest(String pin) {
        super();
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}