package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public class InvalidCommand {
    public static SendMessage execute(Message message) {
        String text = "Такой команды не существует.\nПопробуйте воспользоваться командой /help";
        return new SendMessage(message.chat().id(), text);
    }
}
