package ru.netology;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Restaurant {
    private static final long COOK_MAKES_DISH = 2;
    private static final long VISITOR_MAKES_AN_ORDER = 2;
    private static final long WAITER_DELIVER_THE_ORDER = 2;
    public static final int EXPECT_VISITORS = 5;
    private static final long START = System.nanoTime();
    private static final long WORK_TIME = 30_000_000_000L;
    private final List<Dish> dishes;
    private final List<Visitor> visitors;
    private final List<Order> orders;
    private final Lock locker;
    private final Condition conditionWaiter;
    private final Condition conditionCook;
    private boolean isOpen = true;

    Restaurant() {
        orders = new ArrayList<>();
        dishes = new ArrayList<>();
        visitors = new ArrayList<>();
        locker = new ReentrantLock();
        conditionWaiter = locker.newCondition();
        conditionCook = locker.newCondition();
    }

    public void cookAtWork() {
        locker.lock();
        try {
            System.out.println("Cook at work");
            while (isOpen() || !visitors.isEmpty()) {
                while (orders.isEmpty()) {
                    System.out.println("No orders");
                    conditionCook.await();
                }
                System.out.println("Cook makes dish");
                TimeUnit.SECONDS.sleep(COOK_MAKES_DISH);
                dishes.add(new Dish());
                orders.remove(orders.size() - 1);
                conditionWaiter.signal();
                if ((System.nanoTime() - START) > WORK_TIME) {
                    isOpen = false;
                    System.out.println("Cook go home");
                    return;
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
        try {
            System.out.println(Thread.currentThread().getName() + " waiter can accept the order");
            while (visitors.isEmpty()) {
                System.out.println("No visitors");
                conditionWaiter.await();
            }
            while (dishes.isEmpty()) {
                orders.add(new Order());
                System.out.println(Thread.currentThread().getName() + " order transferred to kitchen");
                conditionCook.signal();
                conditionWaiter.await();
            }

            System.out.println(Thread.currentThread().getName() + " took the order");
            dishes.remove(dishes.size() - 1);
            if (!visitors.isEmpty()) {
                visitors.remove(visitors.size() - 1);
            }
            TimeUnit.SECONDS.sleep(WAITER_DELIVER_THE_ORDER);
            conditionWaiter.signal();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            locker.unlock();
        }
    }

    public void visitorEntrance() {
        locker.lock();
        visitors.add(new Visitor());
        System.out.println(Thread.currentThread().getName() + " at the restaurant");
        System.out.println("Visitor makes an order");
        try {
            TimeUnit.SECONDS.sleep(VISITOR_MAKES_AN_ORDER);
            conditionWaiter.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    public boolean isOpen() {
        return isOpen;
    }
}