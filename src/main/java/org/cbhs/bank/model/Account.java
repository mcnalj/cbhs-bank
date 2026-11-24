package org.cbhs.bank.model;

import java.util.ArrayList;

/**
 * One bank account. Balance rules live here.
 * Students may implement this class in a Unit 3 lab.
 */
public class Account {
    private int accountNumber;
    private String ownerName;
    private String accountType;
    private double balance;
    private ArrayList<Transaction> transactions;

    public Account(int accountNumber, String ownerName, String accountType, double startingBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.accountType = accountType;
        this.balance = startingBalance;
        this.transactions = new ArrayList<Transaction>();
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Adds amount to the balance when amount is greater than 0.
     * @return true if the deposit happened
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance = balance + amount;
        return true;
    }

    /**
     * Removes amount from the balance when amount is greater than 0
     * and not greater than the current balance. No overdraft.
     * @return true if the withdraw happened
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > balance) {
            return false;
        }
        balance = balance - amount;
        return true;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public String toString() {
        return accountNumber + " " + ownerName + " " + accountType + " " + balance;
    }
}
