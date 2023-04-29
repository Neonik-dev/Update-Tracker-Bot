package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

import java.util.Map;

@Getter
public class HelpCommand implements BaseCommand{
    private static final Map<String, BaseCommand> COMMANDS = InitCommands.getAllCommands();
    private static final String NAME = "/help";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return NAME + " -> выводит все доступные команды";
    }
    @Override
    public SendMessage execute(Message message) {
        StringBuilder text = new StringBuilder("Список всех команд:");
        COMMANDS.values().forEach(
                (command) -> text.append("\n").append(command.getDescription())
        );
        return SendSimpleMessage.create(message, text.toString());
    }
}
