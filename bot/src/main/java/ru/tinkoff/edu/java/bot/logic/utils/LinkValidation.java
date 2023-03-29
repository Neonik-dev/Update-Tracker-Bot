package ru.tinkoff.edu.java.bot.logic.utils;

import java.net.URI;

public class LinkValidation {
    public static URI validate(String link) {
        return URI.create(link);
    }
}
