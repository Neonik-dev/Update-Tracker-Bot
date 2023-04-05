package ru.tinkoff.edu.java.scrapper.controllers;


import java.util.HashSet;

public class UserManager {
    private static final HashSet<Long> USER_ID = new HashSet<>();

    public static void add(Long id) {
        USER_ID.add(id);
    }

    public static void remove(Long id) {
        USER_ID.remove(id);
    }
}
