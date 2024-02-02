package com.example.cs202pz.Entity;

import java.io.Serializable;

public class User implements Serializable {
    private final String firstName;
    private final String lastName;
    private final double balance;

    public User(String firstName, String lastName, double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getBalance() {
        return balance;
    }
}
