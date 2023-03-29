package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashMap;

public class HelpCommand implements BaseCommand{
    private final HashMap<String, BaseCommand> COMMANDS = new Commands().getBaseCommands();
    private final String name = "/help";
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return name + " -> выводит все доступные команды";
    }

    @Override
    public SendMessage execute(Message message) {
        StringBuilder text = new StringBuilder("Список всех команд:");
        for (BaseCommand command : COMMANDS.values())
        {
            text.append("\n");
            text.append(command.getDescription());
        }
        return new SendMessage(message.chat().id(), text.toString());
    }
}
