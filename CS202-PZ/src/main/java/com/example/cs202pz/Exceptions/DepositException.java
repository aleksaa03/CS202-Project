package com.example.cs202pz.Exceptions;

/**
 * Custom exception class for handling deposit-related exceptions in the ATM application.
 */
public class DepositException extends Exception {
    public DepositException(String message) {
        super(message);
    }
}
