package org.cbhs.bank.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * All accounts in the school bank.
 * Students may implement this class in Unit 4 labs.
 */
public class Bank {
    public static final double INTEREST_RATE = 0.05;

    private ArrayList<Account> accounts;

    public Bank() {
        accounts = new ArrayList<Account>();
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public int countAccounts() {
        return accounts.size();
    }

    /**
     * Linear search. Returns null if the number is not found.
     */
    public Account findAccount(int accountNumber) {
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }

    /**
     * Case-sensitive substring match on owner name.
     */
    public ArrayList<Account> findAccountsByName(String name) {
        ArrayList<Account> matches = new ArrayList<Account>();
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.getOwnerName().indexOf(name) >= 0) {
                matches.add(account);
            }
        }
        return matches;
    }

    /**
     * Adds the account if its number is not already used.
     */
    public boolean addAccount(Account account) {
        if (findAccount(account.getAccountNumber()) != null) {
            return false;
        }
        accounts.add(account);
        return true;
    }

    /**
     * Largest existing account number + 1, or 1001 if the bank is empty.
     */
    public int nextAccountNumber() {
        if (accounts.size() == 0) {
            return 1001;
        }
        int max = accounts.get(0).getAccountNumber();
        for (int i = 1; i < accounts.size(); i++) {
            int number = accounts.get(i).getAccountNumber();
            if (number > max) {
                max = number;
            }
        }
        return max + 1;
    }

    public boolean deposit(int accountNumber, double amount, String note, String stamp) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            return false;
        }
        boolean ok = account.deposit(amount);
        if (ok) {
            account.addTransaction(new Transaction("deposit", amount, note, stamp));
        }
        return ok;
    }

    public boolean withdraw(int accountNumber, double amount, String note, String stamp) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            return false;
        }
        boolean ok = account.withdraw(amount);
        if (ok) {
            account.addTransaction(new Transaction("withdraw", amount, note, stamp));
        }
        return ok;
    }

    public boolean transfer(int fromNumber, int toNumber, double amount, String note, String stamp) {
        if (fromNumber == toNumber) {
            return false;
        }
        Account from = findAccount(fromNumber);
        Account to = findAccount(toNumber);
        if (from == null || to == null) {
            return false;
        }
        boolean took = from.withdraw(amount);
        if (!took) {
            return false;
        }
        to.deposit(amount);
        from.addTransaction(new Transaction("transfer-out", amount, note, stamp));
        to.addTransaction(new Transaction("transfer-in", amount, note, stamp));
        return true;
    }

    public double totalAssets() {
        double total = 0;
        for (int i = 0; i < accounts.size(); i++) {
            total = total + accounts.get(i).getBalance();
        }
        return total;
    }

    public double averageBalance() {
        if (accounts.size() == 0) {
            return 0;
        }
        return totalAssets() / accounts.size();
    }

    public Account largestAccount() {
        if (accounts.size() == 0) {
            return null;
        }
        Account largest = accounts.get(0);
        for (int i = 1; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.getBalance() > largest.getBalance()) {
                largest = account;
            }
        }
        return largest;
    }

    /**
     * Applies 5% once to each savings account with a positive balance.
     * @return how many accounts received interest
     */
    public int applyInterest(String stamp) {
        int updated = 0;
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.getAccountType().equals("savings") && account.getBalance() > 0) {
                double interest = account.getBalance() * INTEREST_RATE;
                account.deposit(interest);
                account.addTransaction(new Transaction("interest", interest, "Interest", stamp));
                updated++;
            }
        }
        return updated;
    }

    /**
     * Replaces all accounts, then reads the seed file.
     * Format: accountNumber,ownerName,accountType,balance
     */
    public void loadFromFile(String filename) throws IOException {
        accounts = new ArrayList<Account>();
        Scanner input = new Scanner(new File(filename));
        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.length() == 0) {
                continue;
            }
            String[] parts = line.split(",");
            int number = Integer.parseInt(parts[0]);
            String owner = parts[1];
            String type = parts[2];
            double balance = Double.parseDouble(parts[3]);
            addAccount(new Account(number, owner, type, balance));
        }
        input.close();
    }
}
