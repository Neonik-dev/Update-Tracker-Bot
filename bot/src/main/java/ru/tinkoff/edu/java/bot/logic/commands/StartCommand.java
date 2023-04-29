package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

@Getter
@RequiredArgsConstructor
public class StartCommand implements BaseCommand{
    private final ScrapperClient scrapperClient;
    private static final String GREETING = "Добро пожаловать!\nДля списка всех команд напишите команду /help";
    private static final String NAME = "/start";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return NAME + " -> регистрирует пользователя";
    }

    @Override
    public SendMessage execute(Message message) {
        scrapperClient.postChatId(message.chat().id());
        return SendSimpleMessage.create(message, GREETING);
    }
}
