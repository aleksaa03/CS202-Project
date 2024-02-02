package com.example.cs202pz.Exceptions;

import com.example.cs202pz.Common.Constants;

/**
 * Custom exception class for handling invalid PIN exceptions in the ATM application.
 */
public class InvalidPinException extends Exception {
    public InvalidPinException() {
        super(Constants.invalidPin);
    }
}
