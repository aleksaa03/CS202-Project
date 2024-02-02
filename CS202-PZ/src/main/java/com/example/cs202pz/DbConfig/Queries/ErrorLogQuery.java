package com.example.cs202pz.DbConfig.Queries;

public class ErrorLogQuery {
    public static String insertErrorLog() { return "INSERT INTO ErrorLog (Message) VALUES (?)"; }
}
