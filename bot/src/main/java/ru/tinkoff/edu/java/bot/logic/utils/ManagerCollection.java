package ru.tinkoff.edu.java.bot.logic.utils;

import ru.tinkoff.edu.java.bot.logic.exceptions.NotUniqueLinkException;

import java.net.URI;
import java.util.ArrayList;

public class ManagerCollection {
    private static final ArrayList<URI> LINKS = new ArrayList<>();

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

    public static ArrayList<URI> getLinks() {
        return LINKS;
    }
}
