package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import ru.tinkoff.edu.java.bot.logic.wrapper.SendSimpleMessage;

public class InvalidCommand {
    public static SendSimpleMessage execute(Message message) {
        String text = "Такой команды не существует.\nПопробуйте воспользоваться командой /help";
        return new SendSimpleMessage(message.chat().id(), text);
    }
}
