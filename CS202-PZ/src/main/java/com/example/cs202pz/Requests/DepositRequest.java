package com.example.cs202pz.Requests;

import com.example.cs202pz.Common.Enums.TransactionStatus;

/**
 * DepositRequest class represents a request to deposit or withdraw an amount in the ATM application.
 */
public class DepositRequest extends Request {
    private final int id;
    private final boolean isWithdrawal;
    private final double amount;

    private TransactionStatus status = TransactionStatus.PENDING;

    public DepositRequest(int id, double amount, boolean isWithdrawal) {
        super();
        this.id = id;
        this.amount = amount;
        this.isWithdrawal = isWithdrawal;
    }

    public boolean isWithdrawal() {
        return isWithdrawal;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}