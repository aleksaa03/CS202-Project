package com.example.cs202pz.Requests;

/**
 * ErrorLogRequest class represents a request to log an error message in the ATM application.
 */
public class ErrorLogRequest extends Request {
    private String message;

    public ErrorLogRequest() {}

    public ErrorLogRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorLog{" +
                "message='" + message + '\'' +
                '}';
    }
}
