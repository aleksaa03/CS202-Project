package com.example.cs202pz.Entity;

public class ErrorLog {
    private String message;

    public ErrorLog() {}

    public ErrorLog(String message) {
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
