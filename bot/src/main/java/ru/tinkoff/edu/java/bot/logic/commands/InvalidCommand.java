package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

public class InvalidCommand {
    private static final String TEXT = "Такой команды не существует.\nПопробуйте воспользоваться командой /help";
    public static SendMessage execute(Message message) {
        return SendSimpleMessage.create(message, TEXT);
    }
}
