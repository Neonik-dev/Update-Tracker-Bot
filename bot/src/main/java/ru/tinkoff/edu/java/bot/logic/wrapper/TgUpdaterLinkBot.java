package ru.tinkoff.edu.java.bot.logic.wrapper;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.logic.commands.BaseCommand;
import ru.tinkoff.edu.java.bot.logic.commands.InitCommands;
import ru.tinkoff.edu.java.bot.logic.commands.InputHandler;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class TgUpdaterLinkBot implements TgBot {
    private TelegramBot bot;
    private final ApplicationConfig config;
    private final InputHandler inputHandler;

    @Override
    public void start() {
        bot = new TelegramBot(config.token());
        bot.execute(getAllCommands());
        bot.setUpdatesListener(this);
    }

    private SetMyCommands getAllCommands() {
        Map<String, BaseCommand> commands = InitCommands.getAllCommands();
        BotCommand[] botCommands = commands.keySet()
                .stream().map(
                        (value) -> new BotCommand(value, commands.get(value).getDescription())
                ).toArray(BotCommand[]::new);

        return new SetMyCommands(botCommands);
    }

    @Override
    public void close() {
        bot.removeGetUpdatesListener();
    }

    @Override
    public void sendMessage(SendMessage message) {
        bot.execute(message);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            bot.execute(inputHandler.run(update));
        }
        return CONFIRMED_UPDATES_ALL;
    }
}