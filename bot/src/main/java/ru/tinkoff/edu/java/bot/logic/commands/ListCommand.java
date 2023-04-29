package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.scrapper.LinkResponse;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ListCommand implements BaseCommand {
    private final ScrapperClient scrapperClient;
    private static final String LINKS_MISSING = "Пока нет ни одной отслеживаемой ссылки.\nДобавить ссылку можно с помощью команды /track";
    private static final String NAME = "/list";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return NAME + " -> выводит список отслеживающих ссылок";
    }

    @Override
    public SendMessage execute(Message message) {
        List<LinkResponse> links = scrapperClient.getListLinks(message.chat().id()).links();
//        Set<URI> links = ManagerCollection.getLinks();
        String text;
        if (links.isEmpty()) {
            text = LINKS_MISSING;
        } else {
            StringBuilder textLinks = new StringBuilder("Список всех ссылок:");
            links.forEach(
                    (value) -> textLinks.append("\n").append(value.url())
            );
            text = textLinks.toString();
        }
        return SendSimpleMessage.create(message, text);
    }
}
