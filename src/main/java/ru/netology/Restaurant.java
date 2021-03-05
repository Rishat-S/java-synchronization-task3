package ru.netology;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Restaurant {
    private static final long COOK_MAKES_DISH = 5;
    private static final long VISITOR_MAKES_AN_ORDER = 5;
    private int visitors = 0;
    private static int waiters = 0;
    List<Dish> dishes;
    Lock locker;
    Condition condition;

    Restaurant() {
        dishes = new ArrayList<>();
        locker = new ReentrantLock();
        condition = locker.newCondition();
    }

    public void cookAtWork() {
        locker.lock();
        try {
            System.out.println("Cook at work");
            while (true) {
                while (dishes.size() != 0) {
                    condition.await();
                }
                TimeUnit.SECONDS.sleep(COOK_MAKES_DISH);
                dishes.add(new Dish());
//                condition.signalAll();
                if (waiters == 0) {
                    System.out.println("Cook go home");
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
        }

    }

    public void waiterGetOrder() {
        locker.lock();
        waiters++;
        System.out.println("Waiter at work");
        try {
            // no visitors waiting yet
            while (visitors < 1) {
                condition.await();
            }

            visitors--;
            System.out.println("Waiter took the order");
            dishes.remove(dishes.size() -1);
            waiters--;
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
            while (dishes.size() == 0) {
                condition.await();
            }
            visitors++;
            System.out.println("Visitor at the restaurant");
            TimeUnit.SECONDS.sleep(VISITOR_MAKES_AN_ORDER);
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
        }
    }
}