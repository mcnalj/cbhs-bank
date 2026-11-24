package org.cbhs.bank.model;

/**
 * One completed action on an account.
 * Students may implement this class in a later lab.
 */
public class Transaction {
    private String type;
    private double amount;
    private String note;
    private String stamp;

    public Transaction(String type, double amount, String note, String stamp) {
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.stamp = stamp;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getStamp() {
        return stamp;
    }

    public String toString() {
        return stamp + " " + type + " " + amount + " " + note;
    }
}
