package com.example.hvalfisk.notsosuckyhangman;

public class User {
    private int currentStreak;
    private String name;
    private int highestStreak;


    public User(String name) {
        this.name = name;
        currentStreak = 0;
        highestStreak = 0;
    }

    public String getName() {
        return name;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getHighestStreak() {
        return highestStreak;
    }
}
