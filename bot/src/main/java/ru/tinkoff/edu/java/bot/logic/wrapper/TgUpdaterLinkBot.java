package ru.tinkoff.edu.java.bot.logic.wrapper;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.logic.commands.InputHandler;

import java.util.List;


@RequiredArgsConstructor
public class TgUpdaterLinkBot implements TgBot {
    private TelegramBot bot;
    private final ApplicationConfig config;
    private final InputHandler inputHandler;

    @Override
    public void start() {
        bot = new TelegramBot(config.token());
        bot.setUpdatesListener(this);
    }

    @Override
    public void close() {
        bot.removeGetUpdatesListener();
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            bot.execute(inputHandler.run(update));
        }
        return CONFIRMED_UPDATES_ALL;
    }
}