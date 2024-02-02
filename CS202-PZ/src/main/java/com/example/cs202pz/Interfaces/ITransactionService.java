package com.example.cs202pz.Interfaces;

import com.example.cs202pz.Entity.Transaction;
import com.example.cs202pz.Requests.TransactionsRequest;

public interface ITransactionService {
    void getTransactions(TransactionsRequest request);
    void insertTransaction(Transaction transaction);
}
