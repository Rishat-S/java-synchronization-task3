package ru.netology;

public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        int count = 0;

        new Thread(restaurant::waiterGetOrder, "Waiter" + count).start();
        new Thread(restaurant::visitorEntrance, "Visitor" +count).start();
    }
}
