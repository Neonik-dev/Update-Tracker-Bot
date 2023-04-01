package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class HelpCommand implements BaseCommand{
    private static final HashMap<String, BaseCommand> COMMANDS = InitBaseCommands.getCommands();
    private final String description = "выводит все доступные команды";

    @Override
    public SendMessage execute(Message message) {
        StringBuilder text = new StringBuilder("Список всех команд:");
        for (String nameCommand : COMMANDS.keySet())
        {
            text.append("\n");
            text.append(nameCommand);
            text.append(" -> ");
            text.append(COMMANDS.get(nameCommand).getDescription());
        }
        return new SendMessage(message.chat().id(), text.toString());
    }
}
