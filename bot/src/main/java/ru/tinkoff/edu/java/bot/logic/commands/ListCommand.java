package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.logic.utils.ManagerCollection;

import java.net.URI;
import java.util.ArrayList;

public class ListCommand implements BaseCommand {
    private final String name = "/list";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return name + " -> выводит список отслеживающих ссылок";
    }

    @Override
    public SendMessage execute(Message message) {
        ArrayList<URI> links = ManagerCollection.getLinks();
        String text;
        if (links.isEmpty()) {
            text = "Пока нет ни одной отслеживаемой ссылки.\nДобавить ссылку можно с помощью команды /track";
        } else {
            StringBuilder textLinks = new StringBuilder("Список всех ссылок:");
            for (URI uri : links) {
                textLinks.append("\n");
                textLinks.append(uri);
            }
            text = textLinks.toString();
        }
        return new SendMessage(message.chat().id(), text);
    }
}
