package ru.tinkoff.edu.java.bot.logic.utils;

import ru.tinkoff.edu.java.bot.exceptions.NotUniqueLinkException;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class ManagerCollection {
    private static final Set<URI> LINKS = new HashSet<>();

    public static void add(URI link) throws NotUniqueLinkException {
        if (LINKS.contains(link)) {
            throw new NotUniqueLinkException("Данная ссылка уже отслеживается");
        }
        LINKS.add(link);
    }

    public static void remove(URI link) throws NotUniqueLinkException {
        if (!LINKS.contains(link)) {
            throw new NotUniqueLinkException("Данной ссылки нет в списке");
        }
        LINKS.remove(link);
    }

    public static Set<URI> getLinks() {
        return LINKS;
    }
}
