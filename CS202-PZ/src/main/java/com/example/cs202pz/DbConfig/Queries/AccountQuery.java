package com.example.cs202pz.DbConfig.Queries;

public class AccountQuery {
    public static String getAccountById() { return "SELECT account.Amount FROM account WHERE account.Id = ?"; }
    public static String getAccountByPin() { return "SELECT account.Id FROM account WHERE account.Pin = ?"; }
    public static String accountWithdraw() { return "UPDATE account SET account.Amount = account.Amount - ? WHERE account.Id = ?"; }
    public static String accountDeposit() { return "UPDATE account SET account.Amount = account.Amount + ? WHERE account.Id = ?"; }
}
