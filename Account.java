package com.bank;

import java.util.concurrent.atomic.AtomicInteger;

public class Account {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private final int id;
    private int balance;

    public Account(int initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Початковий баланс не може бути від'ємним");
        }
        this.id = ID_GENERATOR.incrementAndGet();
        this.balance = initialBalance;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void withdraw(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сума зняття не може бути від'ємною");
        }
        if (balance < amount) {
            throw new IllegalStateException("Недостатньо коштів на рахунку #" + id);
        }
        balance -= amount;
    }

    public void deposit(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сума поповнення не може бути від'ємною");
        }
        balance += amount;
    }
}
