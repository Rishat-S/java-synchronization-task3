package ru.netology;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Restaurant restaurant = new Restaurant();
        int count = 0;

        new Thread(restaurant::waiterGetOrder, "Waiter" + count).start();
        new Thread(restaurant::cookAtWork, "Cook").start();
        new Thread(restaurant::visitorEntrance, "Visitor" +count).start();
    }
}
