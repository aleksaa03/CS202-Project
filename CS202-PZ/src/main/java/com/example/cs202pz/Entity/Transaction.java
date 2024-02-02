package com.example.cs202pz.Entity;

import com.example.cs202pz.Common.Enums.TransactionStatus;

import java.io.Serializable;

public class Transaction implements Serializable {
    private final int id;
    private final String date;
    private final double amount;
    private final double amountBefore;
    private final TransactionStatus status;

    public Transaction(int id, String date, double amount, double amountBefore, int status) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.amountBefore = amountBefore;
        this.status = status == 0 ? TransactionStatus.APPROVED : TransactionStatus.DECLINED;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public double getAmountBefore() {
        return amountBefore;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
               "id=" + id +
               ", date='" + date + '\'' +
               ", amount=" + amount +
               ", amountBefore=" + amountBefore +
               ", status='" + status + '\'' +
               '}';
    }
}