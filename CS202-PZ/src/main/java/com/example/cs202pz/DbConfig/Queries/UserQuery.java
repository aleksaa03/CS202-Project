package com.example.cs202pz.DbConfig.Queries;

public class UserQuery {
    public static String getUserByAccountId() {return "SELECT user.Firstname, user.Lastname, account.Amount FROM account JOIN user ON user.Id = account.UserId WHERE account.Id = ?"; }
}
