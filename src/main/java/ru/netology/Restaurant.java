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
    private static final int EXPECT_VISITORS = 5;
    private final List<Dish> dishes;
    private final List<Visitor> visitors;
    private final List<Order> orders;
    private final Lock locker;
    private final Condition conditionWaiter;
    private final Condition conditionCook;
//    private final Condition conditionVisitor;

    Restaurant() {
        orders = new ArrayList<>();
        dishes = new ArrayList<>();
        visitors = new ArrayList<>();
        locker = new ReentrantLock();
        conditionWaiter = locker.newCondition();
        conditionCook = locker.newCondition();
//        conditionVisitor = locker.newCondition();
    }

    public void cookAtWork() {
        locker.lock();
        try {
            System.out.println("Cook at work");
            for (int i = 0; i < EXPECT_VISITORS; i++) {
                while (orders.isEmpty()) {
                    System.out.println("No orders");
                    conditionCook.await();
                }
                System.out.println("Cook makes dish");
                TimeUnit.SECONDS.sleep(COOK_MAKES_DISH);
                dishes.add(new Dish());
                orders.remove(orders.size() - 1);
                conditionWaiter.signalAll();
            }
            System.out.println("Cook go home");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    public void waiterGetOrder() {
        locker.lock();
        System.out.println(Thread.currentThread().getName() + " at work");
        try {
            while (visitors.isEmpty()) {
                System.out.println("No visitors");
                conditionWaiter.await();
            }
            while (dishes.isEmpty()) {
                System.out.println("No dishes");
                orders.add(new Order());
                conditionCook.signal();
                conditionWaiter.await();
            }

            System.out.println(Thread.currentThread().getName() + " took the order");
            dishes.remove(dishes.size() - 1);
            visitors.remove(visitors.size() - 1);
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
        try {
            System.out.println(Thread.currentThread().getName() + " at the restaurant");
            System.out.println("Visitor makes an order");
            TimeUnit.SECONDS.sleep(VISITOR_MAKES_AN_ORDER);
            conditionWaiter.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
        }
    }
}