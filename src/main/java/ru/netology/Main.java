package ru.netology;

public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        int count = 0;

        new Thread(restaurant::cookAtWork, "Cook").start();

        do {
            count++;
            new Thread(restaurant::waiterGetOrder, "Waiter " + count).start();
        } while (count < 3);
        count = 0;
        do {
            count++;
            new Thread(restaurant::visitorEntrance, "Visitor " + count).start();
        } while (count < 3);
    }
}
