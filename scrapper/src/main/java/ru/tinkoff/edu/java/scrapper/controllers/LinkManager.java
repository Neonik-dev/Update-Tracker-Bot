package ru.tinkoff.edu.java.scrapper.controllers;

import java.net.URI;
import java.util.HashSet;

public class LinkManager {
    private static final HashSet<URI> LINKS = new HashSet<>();

    public static void add(URI link) {
        LINKS.add(link);
    }

    public static void remove(URI link) {
        LINKS.remove(link);
    }

    public static HashSet<URI> getLinks() {
        return LINKS;
    }
}
