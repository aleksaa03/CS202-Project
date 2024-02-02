package com.example.cs202pz.Requests;

import com.example.cs202pz.Entity.User;

/**
 * UserRequest class represents a request for retrieving user information associated
 * with a specific user account in the ATM application.
 */
public class UserRequest extends Request {
    private int id;
    private User user = null;

    public UserRequest(int id) {
        super();
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(String firstName, String lastName, double balance) {
        this.user = new User(firstName, lastName, balance);
    }

    public int getId() {
        return id;
    }
}