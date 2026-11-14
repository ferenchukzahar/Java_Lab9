package com.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankTest {
    public static void main(String[] args) throws InterruptedException {
        final int ACCOUNTS_COUNT = 200;
        final int THREADS_COUNT = 5000;
        final int MAX_INITIAL_BALANCE = 10000;
        final int MAX_TRANSFER_AMOUNT = 500;

        Bank bank = new Bank();
        List<Account> accounts = new ArrayList<>(ACCOUNTS_COUNT);
        Random random = new Random();

        for (int i = 0; i < ACCOUNTS_COUNT; i++) {
            accounts.add(new Account(random.nextInt(MAX_INITIAL_BALANCE + 1)));
        }

        long totalBefore = getTotalBankBalance(accounts);
        System.out.println("Кількість рахунків у банку: " + ACCOUNTS_COUNT);
        System.out.println("Загальна сума в банку ДО переказів:    " + totalBefore);

        ExecutorService executor = Executors.newFixedThreadPool(100);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREADS_COUNT);

        for (int i = 0; i < THREADS_COUNT; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    Account from = accounts.get(random.nextInt(ACCOUNTS_COUNT));
                    Account to = accounts.get(random.nextInt(ACCOUNTS_COUNT));
                    int amount = random.nextInt(MAX_TRANSFER_AMOUNT) + 1;
                    bank.transfer(from, to, amount);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        doneLatch.await();
        long duration = System.currentTimeMillis() - startTime;
        executor.shutdown();

        long totalAfter = getTotalBankBalance(accounts);
