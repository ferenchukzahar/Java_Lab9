package com.bank;

public class Bank {
    public void transfer(Account from, Account to, int amount) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Рахунки не можуть бути null");
        }
        if (amount <= 0 || from.getId() == to.getId()) {
            return;
        }

        Account firstLock = from.getId() < to.getId() ? from : to;
        Account secondLock = from.getId() < to.getId() ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (from.getBalance() >= amount) {
                    from.withdraw(amount);
                    to.deposit(amount);
                }
            }
        }
    }
}
