package ru.tinkoff.edu.java.bot.logic;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.logic.utils.InputHandler;
import ru.tinkoff.edu.java.bot.logic.wrapper.TgBot;

import java.util.List;


public class TgUpdaterLinkBot implements TgBot {
    private TelegramBot bot;
    private final ApplicationConfig config;
    private final InputHandler inputHandler = new InputHandler();

    public TgUpdaterLinkBot(ApplicationConfig config) {
        this.config = config;
    }

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
        inputHandler.run(bot, updates);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}