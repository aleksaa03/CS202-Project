package com.example.cs202pz.DbConfig.Queries;

public class TransactionQuery {
    public static String getTransactionByAccountId() { return "SELECT * FROM transaction WHERE transaction.AccountId = ?"; }
    public static String insertTransaction() { return "INSERT INTO transaction (AccountId, Date, Amount, AmountBefore, Status) VALUES (?, ?, ?, ?, ?)"; }
}
