package ru.netology;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Restaurant {
    private int visitor = 0;
    Lock locker;
    Condition condition;

    Restaurant() {
        locker = new ReentrantLock();
        condition = locker.newCondition();
    }

    public void waiterGetOrder() {
        locker.lock();
        System.out.println("Waiter at work");
        try {
            // no visitors waiting yet
            while (visitor < 1) {
                condition.await();
            }

            visitor--;
            System.out.println("Waiter took the order");
            // we signal
            condition.signalAll();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            locker.unlock();
        }
    }

    public void visitorEntrance() {
        locker.lock();
        try {
            visitor++;
            System.out.println("Visitor at the restaurant");
            condition.signalAll();
        } finally {
            locker.unlock();
        }
    }
}