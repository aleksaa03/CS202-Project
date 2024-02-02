package com.example.cs202pz.Exceptions;

/**
 * Custom exception class for handling withdrawal-related exceptions in the ATM application.
 */
public class WithdrawException extends Exception {
    public WithdrawException(String message) {
        super(message);
    }
}
