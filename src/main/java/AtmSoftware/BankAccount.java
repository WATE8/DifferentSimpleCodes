package AtmSoftware;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private String owner;
    private String pinCode;
    private double balance;
    private List<Transaction> transactions;

    public BankAccount(String owner, String pinCode, double balance) {
        this.owner = owner;
        this.pinCode = pinCode;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public boolean checkPin(String inputPin) {
        return this.pinCode.equals(inputPin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Пополнение", amount));
    }

    public boolean withdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        transactions.add(new Transaction("Снятие", amount));
        return true;
    }

    public void printTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("История операций пуста.");
            return;
        }
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public String getOwner() {
        return owner;
    }
}
