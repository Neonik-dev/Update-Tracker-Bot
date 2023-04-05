package ru.tinkoff.edu.java.bot.logic.utils;

import ru.tinkoff.edu.java.bot.exceptions.InvalidLinkException;

import java.net.URI;

public class LinkValidation {
    public static URI validate(String link) throws InvalidLinkException {
        try {
            return URI.create(link);
        } catch (Exception e) {
            throw new InvalidLinkException("Введенная строка не является ссылкой");
        }
    }
}
