package com.ringbuffer;

import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumerApp {
    public static void main(String[] args) throws InterruptedException {
        final int BUFFER_CAPACITY = 10;
        final int PRODUCERS_COUNT = 5;
        final int TRANSLATORS_COUNT = 2;
        final int MESSAGES_TO_READ = 100;

        RingBuffer<String> rawBuffer = new RingBuffer<>(BUFFER_CAPACITY);
        RingBuffer<String> translatedBuffer = new RingBuffer<>(BUFFER_CAPACITY);
        AtomicInteger messageCounter = new AtomicInteger(1);

        for (int i = 1; i <= PRODUCERS_COUNT; i++) {
            final int producerId = i;
            Thread producer = new Thread(() -> {
                try {
                    while (true) {
                        int msgNum = messageCounter.getAndIncrement();
                        String msg = "Потік №" + producerId + " згенерував повідомлення #" + msgNum;
                        rawBuffer.put(msg);
                        Thread.sleep(5);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer-" + producerId);
            producer.setDaemon(true);
            producer.start();
        }

        for (int i = 1; i <= TRANSLATORS_COUNT; i++) {
            final int translatorId = i;
            Thread translator = new Thread(() -> {
                try {
                    while (true) {
                        String originalMsg = rawBuffer.take();
                        String translatedMsg = "Потік №" + translatorId + " переклав повідомлення [" + originalMsg + "]";
                        translatedBuffer.put(translatedMsg);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Translator-" + translatorId);
            translator.setDaemon(true);
            translator.start();
        }

        for (int i = 1; i <= MESSAGES_TO_READ; i++) {
            String finalMsg = translatedBuffer.take();
            System.out.println(i + ". " + finalMsg);
        }
