package ru.netology;

public class Main {
    private static final int N_WAITERS = 2;

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();

        new Thread(restaurant::cookAtWork, "Cook").start();

        for (int count = 1; count <= N_WAITERS; count++) {
            new Thread(new Waiter(restaurant), "Waiter " + count).start();
        }

        for (int count = 1; count <= Restaurant.EXPECT_VISITORS; count++) {
            new Thread(restaurant::visitorEntrance, "Visitor " + count).start();
        }

    }
}
