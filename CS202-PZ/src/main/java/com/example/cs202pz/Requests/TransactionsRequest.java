package com.example.cs202pz.Requests;

import com.example.cs202pz.Entity.Transaction;

import java.util.ArrayList;

/**
 * TransactionsRequest class represents a request for retrieving transaction history
 * associated with a specific user account in the ATM application.
 */
public class TransactionsRequest extends Request {
    private int id = 0;

    public TransactionsRequest() {
        super();
    }

    public TransactionsRequest(int id) {
        super();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private ArrayList<Transaction> transactions = new ArrayList<>();

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}