package ru.netology;

public class Waiter implements Runnable {
    Restaurant restaurant;

    public Waiter(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " at work");
        while (restaurant.isOpen()) {
            restaurant.waiterGetOrder();
        }
    }
}
