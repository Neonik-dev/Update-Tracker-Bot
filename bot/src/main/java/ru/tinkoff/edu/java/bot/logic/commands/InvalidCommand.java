package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

public class InvalidCommand {
    private static final String TEXT = "Такой команды не существует.\nПопробуйте воспользоваться командой /help";
    public static SendSimpleMessage execute(Message message) {
        return new SendSimpleMessage(message.chat().id(), TEXT);
    }
}
