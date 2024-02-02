package com.example.cs202pz.Interfaces;

import com.example.cs202pz.Requests.DepositRequest;

public interface IAccountService {
    double getBalance(int id);
    void withdraw(DepositRequest request) throws Exception;
    void updateAmount(DepositRequest request, double balance, String query) throws Exception;
    void deposit(DepositRequest request) throws Exception;
}
