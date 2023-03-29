package ru.tinkoff.edu.java.bot.logic;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.logic.utils.InputHandler;

import java.util.List;

public class ConnectingBot {
    private final TelegramBot bot;

    public ConnectingBot(ApplicationConfig config) {
        bot = new TelegramBot(config.token());
        bot.setUpdatesListener(new UpdatesListener() {
            private final InputHandler inputHandler = new InputHandler();
            @Override
            public int process(List<Update> updates) {
                inputHandler.run(bot, updates);
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }
}
