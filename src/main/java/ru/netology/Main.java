package ru.netology;

public class Main {

    private static final int N_WAITERS = 5;
    private static final int N_VISITORS = 5;

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        int count = 0;

        new Thread(restaurant::cookAtWork, "Cook").start();

        do {
            count++;
            new Thread(restaurant::waiterGetOrder, "Waiter " + count).start();
        } while (count < N_WAITERS);
        count = 0;
        do {
            count++;
            new Thread(restaurant::visitorEntrance, "Visitor " + count).start();
        } while (count < N_VISITORS);
    }
}
