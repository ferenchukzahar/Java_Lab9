
package com.ringbuffer;

public class RingBuffer<T> {
    private static class Node<T> {
        T value;
        Node<T> next;
    }

    private Node<T> head;
    private Node<T> tail;

    public RingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Місткість буфера має бути більшою за 0");
        }
        Node<T> first = new Node<>();
        Node<T> current = first;
        for (int i = 0; i < capacity; i++) {
            current.next = new Node<>();
            current = current.next;
        }
        current.next = first;

        this.head = first;
        this.tail = first;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (tail.next == head) {
            wait();
        }
        tail.value = item;
        tail = tail.next;
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (head == tail) {
            wait();
        }
        T item = head.value;
        head.value = null;
        head = head.next;
        notifyAll();
        return item;
    }
}
